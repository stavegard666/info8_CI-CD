package com.epita.reposocial.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.epita.reposocial.entity.BlockEntity;
import com.epita.reposocial.entity.FollowEntity;
import com.epita.reposocial.entity.LikeEntity;
import com.epita.reposocial.service.SocialServiceImpl;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

@ExtendWith(MockitoExtension.class)
class SocialControllerTest {

    @Mock
    SocialServiceImpl socialService;

    @InjectMocks
    SocialController controller;

    private UUID userId;
    private UUID postId;
    private UUID followerId;
    private UUID followedId;
    private UUID blockerId;
    private UUID blockedId;
    private LikeEntity likeEntity;
    private FollowEntity followEntity;
    private BlockEntity blockEntity;

    @BeforeEach
    void setup() {
        userId = UUID.randomUUID();
        postId = UUID.randomUUID();
        followerId = UUID.randomUUID();
        followedId = UUID.randomUUID();
        blockerId = UUID.randomUUID();
        blockedId = UUID.randomUUID();

        likeEntity = new LikeEntity(userId, postId);
        followEntity = new FollowEntity(followerId, followedId);
        blockEntity = new BlockEntity(blockerId, blockedId);
    }

    // Like endpoint tests
    @Test
    void testLikePost() {
        // Given
        when(socialService.likePost(userId, postId)).thenReturn(likeEntity);

        // When
        Response response = controller.likePost(userId.toString(), postId.toString());

        // Then
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals(likeEntity, response.getEntity());
    }

    @Test
    void testLikePostWithInvalidUUID() {
        // When
        Response response = controller.likePost("invalid-uuid", postId.toString());

        // Then
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    void testLikePostWithWebApplicationException() {
        // Given
        when(socialService.likePost(userId, postId))
                .thenThrow(new WebApplicationException("Already liked", Response.Status.CONFLICT));

        // When
        Response response = controller.likePost(userId.toString(), postId.toString());

        // Then
        assertEquals(Response.Status.CONFLICT.getStatusCode(), response.getStatus());
    }

    @Test
    void testUnlikePost() {
        // When
        Response response = controller.unlikePost(userId.toString(), postId.toString());

        // Then
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }

    @Test
    void testGetLikingUsers() {
        // Given
        List<UUID> users = Arrays.asList(userId);
        when(socialService.getLikingUsers(postId)).thenReturn(users);

        // When
        Response response = controller.getLikingUsers(postId.toString());

        // Then
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(users, response.getEntity());
    }

    @Test
    void testGetUserLikedPosts() {
        // Given
        List<UUID> posts = Arrays.asList(postId);
        when(socialService.getUserLikedPosts(userId)).thenReturn(posts);

        // When
        Response response = controller.getUserLikedPosts(userId.toString());

        // Then
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(posts, response.getEntity());
    }

    // Follow endpoint tests
    @Test
    void testFollowUser() {
        // Given
        when(socialService.followUser(followerId, followedId)).thenReturn(followEntity);

        // When
        Response response = controller.followUser(followerId.toString(), followedId.toString());

        // Then
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals(followEntity, response.getEntity());
    }

    @Test
    void testFollowUserWithBlockException() {
        // Given
        when(socialService.followUser(followerId, followedId))
                .thenThrow(new WebApplicationException("Cannot follow: block relationship exists",
                        Response.Status.FORBIDDEN));

        // When
        Response response = controller.followUser(followerId.toString(), followedId.toString());

        // Then
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }

    @Test
    void testUnfollowUser() {
        // When
        Response response = controller.unfollowUser(followerId.toString(), followedId.toString());

        // Then
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }

    @Test
    void testGetUserFollowers() {
        // Given
        List<UUID> followers = Arrays.asList(followerId);
        when(socialService.getUserFollowers(followedId)).thenReturn(followers);

        // When
        Response response = controller.getUserFollowers(followedId.toString());

        // Then
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(followers, response.getEntity());
    }

    @Test
    void testGetUserFollowing() {
        // Given
        List<UUID> following = Arrays.asList(followedId);
        when(socialService.getUserFollowing(followerId)).thenReturn(following);

        // When
        Response response = controller.getUserFollowing(followerId.toString());

        // Then
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(following, response.getEntity());
    }

    // Block endpoint tests
    @Test
    void testBlockUser() {
        // Given
        when(socialService.blockUser(blockerId, blockedId)).thenReturn(blockEntity);

        // When
        Response response = controller.blockUser(blockerId.toString(), blockedId.toString());

        // Then
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals(blockEntity, response.getEntity());
    }

    @Test
    void testUnblockUser() {
        // When
        Response response = controller.unblockUser(blockerId.toString(), blockedId.toString());

        // Then
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }

    @Test
    void testGetUserBlocked() {
        // Given
        List<UUID> blocked = Arrays.asList(blockedId);
        when(socialService.getUserBlocked(blockerId)).thenReturn(blocked);

        // When
        Response response = controller.getUserBlocked(blockerId.toString());

        // Then
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(blocked, response.getEntity());
    }

    @Test
    void testGetUserBlockers() {
        // Given
        List<UUID> blockers = Arrays.asList(blockerId);
        when(socialService.getUserBlockers(blockedId)).thenReturn(blockers);

        // When
        Response response = controller.getUserBlockers(blockedId.toString());

        // Then
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(blockers, response.getEntity());
    }
}
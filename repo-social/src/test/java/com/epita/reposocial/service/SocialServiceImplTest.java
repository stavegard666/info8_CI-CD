package com.epita.reposocial.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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

import com.epita.contracts.BlocksUserContract;
import com.epita.contracts.FollowsContract;
import com.epita.contracts.LikesContract;
import com.epita.reposocial.entity.BlockEntity;
import com.epita.reposocial.entity.FollowEntity;
import com.epita.reposocial.entity.LikeEntity;
import com.epita.reposocial.mapper.SocialMapper;
import com.epita.reposocial.repository.BlockRepositoryImpl;
import com.epita.reposocial.repository.FollowRepositoryImpl;
import com.epita.reposocial.repository.LikeRepositoryImpl;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.WebApplicationException;

@ExtendWith(MockitoExtension.class)
class SocialServiceImplTest {

    @Mock
    LikeRepositoryImpl likeRepository;

    @Mock
    FollowRepositoryImpl followRepository;

    @Mock
    BlockRepositoryImpl blockRepository;

    @Mock
    RedisMessagingService redisMessagingService;

    @Mock
    SocialMapper socialMapper;

    @InjectMocks
    SocialServiceImpl service;

    private UUID userId;
    private UUID postId;
    private UUID followerId;
    private UUID followedId;
    private UUID blockerId;
    private UUID blockedId;
    private LikeEntity likeEntity;
    private FollowEntity followEntity;
    private BlockEntity blockEntity;
    private LikesContract likesContract;
    private FollowsContract followsContract;
    private BlocksUserContract blocksContract;

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

        likesContract = new LikesContract();
        followsContract = new FollowsContract();
        blocksContract = new BlocksUserContract();

        when(socialMapper.toLikesContract(any(LikeEntity.class))).thenReturn(likesContract);
        when(socialMapper.toFollowsContract(any(FollowEntity.class))).thenReturn(followsContract);
        when(socialMapper.toBlocksUserContract(any(BlockEntity.class))).thenReturn(blocksContract);

        when(redisMessagingService.publishLikeCreated(any(LikesContract.class)))
                .thenReturn(Uni.createFrom().voidItem());
        when(redisMessagingService.publishLikeDeleted(any(UUID.class), any(UUID.class)))
                .thenReturn(Uni.createFrom().voidItem());
        when(redisMessagingService.publishFollowCreated(any(FollowsContract.class)))
                .thenReturn(Uni.createFrom().voidItem());
        when(redisMessagingService.publishFollowDeleted(any(UUID.class), any(UUID.class)))
                .thenReturn(Uni.createFrom().voidItem());
        when(redisMessagingService.publishBlockCreated(any(BlocksUserContract.class)))
                .thenReturn(Uni.createFrom().voidItem());
        when(redisMessagingService.publishBlockDeleted(any(UUID.class), any(UUID.class)))
                .thenReturn(Uni.createFrom().voidItem());
    }

    // Like tests
    @Test
    void testLikePost() {
        // Given
        when(blockRepository.hasBlockRelationship(any(UUID.class), any(UUID.class))).thenReturn(false);
        when(likeRepository.exists(userId, postId)).thenReturn(false);

        // When
        LikeEntity result = service.likePost(userId, postId);

        // Then
        assertNotNull(result);
        assertEquals(userId, result.userId);
        assertEquals(postId, result.postId);
        verify(likeRepository).persist(any(LikeEntity.class));
        verify(redisMessagingService).publishLikeCreated(any(LikesContract.class));
    }

    @Test
    void testLikePostWithExistingLike() {
        // Given
        when(blockRepository.hasBlockRelationship(any(UUID.class), any(UUID.class))).thenReturn(false);
        when(likeRepository.exists(userId, postId)).thenReturn(true);

        // When & Then
        assertThrows(WebApplicationException.class, () -> {
            service.likePost(userId, postId);
        });
        verify(likeRepository, never()).persist(any(LikeEntity.class));
    }

    @Test
    void testLikePostWithBlockRelationship() {
        // Given
        when(blockRepository.hasBlockRelationship(any(UUID.class), any(UUID.class))).thenReturn(true);

        // When & Then
        assertThrows(WebApplicationException.class, () -> {
            service.likePost(userId, postId);
        });
        verify(likeRepository, never()).persist(any(LikeEntity.class));
    }

    @Test
    void testUnlikePost() {
        // When
        service.unlikePost(userId, postId);

        // Then
        verify(likeRepository).deleteLike(userId, postId);
        verify(redisMessagingService).publishLikeDeleted(userId, postId);
    }

    @Test
    void testGetLikingUsers() {
        // Given
        LikeEntity like = new LikeEntity(userId, postId);
        when(likeRepository.findByPostId(postId)).thenReturn(Arrays.asList(like));

        // When
        List<UUID> result = service.getLikingUsers(postId);

        // Then
        assertEquals(1, result.size());
        assertEquals(userId, result.get(0));
    }

    @Test
    void testGetUserLikedPosts() {
        // Given
        LikeEntity like = new LikeEntity(userId, postId);
        when(likeRepository.findByUserId(userId)).thenReturn(Arrays.asList(like));

        // When
        List<UUID> result = service.getUserLikedPosts(userId);

        // Then
        assertEquals(1, result.size());
        assertEquals(postId, result.get(0));
    }

    // Follow tests
    @Test
    void testFollowUser() {
        // Given
        when(blockRepository.hasBlockRelationship(followerId, followedId)).thenReturn(false);
        when(followRepository.exists(followerId, followedId)).thenReturn(false);

        // When
        FollowEntity result = service.followUser(followerId, followedId);

        // Then
        assertNotNull(result);
        assertEquals(followerId, result.followerId);
        assertEquals(followedId, result.followedId);
        verify(followRepository).persist(any(FollowEntity.class));
        verify(redisMessagingService).publishFollowCreated(any(FollowsContract.class));
    }

    @Test
    void testFollowUserWithExistingFollow() {
        // Given
        when(blockRepository.hasBlockRelationship(followerId, followedId)).thenReturn(false);
        when(followRepository.exists(followerId, followedId)).thenReturn(true);

        // When & Then
        assertThrows(WebApplicationException.class, () -> {
            service.followUser(followerId, followedId);
        });
        verify(followRepository, never()).persist(any(FollowEntity.class));
    }

    @Test
    void testFollowUserWithBlockRelationship() {
        // Given
        when(blockRepository.hasBlockRelationship(followerId, followedId)).thenReturn(true);

        // When & Then
        assertThrows(WebApplicationException.class, () -> {
            service.followUser(followerId, followedId);
        });
        verify(followRepository, never()).persist(any(FollowEntity.class));
    }

    @Test
    void testUnfollowUser() {
        // When
        service.unfollowUser(followerId, followedId);

        // Then
        verify(followRepository).deleteFollow(followerId, followedId);
        verify(redisMessagingService).publishFollowDeleted(followerId, followedId);
    }

    // Block tests
    @Test
    void testBlockUser() {
        // When
        BlockEntity result = service.blockUser(blockerId, blockedId);

        // Then
        assertNotNull(result);
        assertEquals(blockerId, result.blockerId);
        assertEquals(blockedId, result.blockedId);
        verify(blockRepository).persist(any(BlockEntity.class));
        verify(redisMessagingService).publishBlockCreated(any(BlocksUserContract.class));
    }

    @Test
    void testUnblockUser() {
        // When
        service.unblockUser(blockerId, blockedId);

        // Then
        verify(blockRepository).deleteBlock(blockerId, blockedId);
        verify(redisMessagingService).publishBlockDeleted(blockerId, blockedId);
    }
}
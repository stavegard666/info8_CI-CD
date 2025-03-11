package com.epita.reposocial.service;

import com.epita.reposocial.entity.BlockEntity;
import com.epita.reposocial.entity.FollowEntity;
import com.epita.reposocial.entity.LikeEntity;
import com.epita.reposocial.repository.BlockRepositoryImpl;
import com.epita.reposocial.repository.FollowRepositoryImpl;
import com.epita.reposocial.repository.LikeRepositoryImpl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class SocialServiceImpl {

    @Inject
    LikeRepositoryImpl likeRepository;

    @Inject
    FollowRepositoryImpl followRepository;

    @Inject
    BlockRepositoryImpl blockRepository;

    // Like operations
    public LikeEntity likePost(String userId, String postId) {
        if (likeRepository.exists(userId, postId)) {
            throw new WebApplicationException("Already liked", Response.Status.CONFLICT);
        }
        LikeEntity like = new LikeEntity(userId, postId);
        likeRepository.persist(like);
        return like;
    }

    public void unlikePost(String userId, String postId) {
        likeRepository.deleteLike(userId, postId);
    }

    public List<String> getLikingUsers(String postId) {
        return likeRepository.findByPostId(postId)
                .stream()
                .map(like -> like.userId)
                .collect(Collectors.toList());
    }

    public List<String> getUserLikedPosts(String userId) {
        return likeRepository.findByUserId(userId)
                .stream()
                .map(like -> like.postId)
                .collect(Collectors.toList());
    }

    // Follow operations
    public FollowEntity followUser(String followerId, String followedId) {
        if (blockRepository.hasBlockRelationship(followerId, followedId)) {
            throw new WebApplicationException("Cannot follow: block relationship exists", 
                                           Response.Status.FORBIDDEN);
        }
        if (followRepository.exists(followerId, followedId)) {
            throw new WebApplicationException("Already following", Response.Status.CONFLICT);
        }
        FollowEntity follow = new FollowEntity(followerId, followedId);
        followRepository.persist(follow);
        return follow;
    }

    public void unfollowUser(String followerId, String followedId) {
        followRepository.deleteFollow(followerId, followedId);
    }

    public List<String> getUserFollowers(String userId) {
        return followRepository.findFollowersByUserId(userId)
                .stream()
                .map(follow -> follow.followerId)
                .collect(Collectors.toList());
    }

    public List<String> getUserFollowing(String userId) {
        return followRepository.findFollowedByUserId(userId)
                .stream()
                .map(follow -> follow.followedId)
                .collect(Collectors.toList());
    }

    // Block operations
    public BlockEntity blockUser(String blockerId, String blockedId) {
        BlockEntity block = new BlockEntity(blockerId, blockedId);
        blockRepository.persist(block);
        // Asynchronously remove follow relationships
        followRepository.deleteAllFollowsBetweenUsers(blockerId, blockedId);
        return block;
    }

    public void unblockUser(String blockerId, String blockedId) {
        blockRepository.deleteBlock(blockerId, blockedId);
    }

    public List<String> getUserBlocked(String userId) {
        return blockRepository.findBlockedByUserId(userId)
                .stream()
                .map(block -> block.blockedId)
                .collect(Collectors.toList());
    }

    public List<String> getUserBlockers(String userId) {
        return blockRepository.findBlockersByUserId(userId)
                .stream()
                .map(block -> block.blockerId)
                .collect(Collectors.toList());
    }
} 
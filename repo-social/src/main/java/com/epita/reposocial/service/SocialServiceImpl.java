package com.epita.reposocial.service;

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
import com.epita.reposocial.repository.Neo4jSocialRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class SocialServiceImpl {
    private static final Logger LOG = Logger.getLogger(SocialServiceImpl.class);

    @Inject
    LikeRepositoryImpl likeRepository;

    @Inject
    FollowRepositoryImpl followRepository;

    @Inject
    BlockRepositoryImpl blockRepository;

    @Inject
    Neo4jSocialRepository neo4jRepository;

    @Inject
    RedisMessagingService redisMessagingService;

    @Inject
    SocialMapper socialMapper;

    // Like operations
    public LikeEntity likePost(UUID userId, UUID postId) {
        // Check if there's a block relationship
        if (hasBlockRelationshipBetweenUserAndPost(userId, postId)) {
            LOG.warn("Like attempt blocked: User " + userId + " has block relationship with post owner " + postId);
            throw new WebApplicationException("Cannot like: block relationship exists",
                    Response.Status.FORBIDDEN);
        }

        if (likeRepository.exists(userId, postId)) {
            LOG.warn("User " + userId + " already liked post " + postId);
            throw new WebApplicationException("Already liked", Response.Status.CONFLICT);
        }

        LikeEntity like = new LikeEntity(userId, postId);
        likeRepository.persist(like);

        // Publish event asynchronously
        LikesContract likeContract = socialMapper.toLikesContract(like);
        redisMessagingService.publishLikeCreated(likeContract)
                .subscribe().with(
                        success -> LOG.debug("Like event published successfully"),
                        failure -> LOG.error("Failed to publish like event", failure));

        return like;
    }

    public void unlikePost(UUID userId, UUID postId) {
        LOG.info("User " + userId + " is unliking post " + postId);
        likeRepository.deleteLike(userId, postId);

        // Publish event asynchronously
        redisMessagingService.publishLikeDeleted(userId, postId)
                .subscribe().with(
                        success -> LOG.debug("Unlike event published successfully"),
                        failure -> LOG.error("Failed to publish unlike event", failure));
    }

    public List<UUID> getLikingUsers(UUID postId) {
        return likeRepository.findByPostId(postId)
                .stream()
                .map(like -> UUID.fromString(like.userId))
                .collect(Collectors.toList());
    }

    public List<UUID> getUserLikedPosts(UUID userId) {
        return likeRepository.findByUserId(userId)
                .stream()
                .map(like -> UUID.fromString(like.postId))
                .collect(Collectors.toList());
    }

    // Follow operations
    public FollowEntity followUser(UUID followerId, UUID followedId) {
        if (blockRepository.hasBlockRelationship(followerId, followedId)) {
            LOG.warn("Follow attempt blocked: Block relationship exists between " + followerId + " and " + followedId);
            throw new WebApplicationException("Cannot follow: block relationship exists",
                    Response.Status.FORBIDDEN);
        }

        if (followRepository.exists(followerId, followedId)) {
            LOG.warn("User " + followerId + " already follows " + followedId);
            throw new WebApplicationException("Already following", Response.Status.CONFLICT);
        }

        FollowEntity follow = new FollowEntity(followerId, followedId);
        followRepository.persist(follow);

        // Update Neo4j graph
        neo4jRepository.createFollow(followerId, followedId);

        // Publish event asynchronously
        FollowsContract followContract = socialMapper.toFollowsContract(follow);
        redisMessagingService.publishFollowCreated(followContract)
                .subscribe().with(
                        success -> LOG.debug("Follow event published successfully"),
                        failure -> LOG.error("Failed to publish follow event", failure));

        return follow;
    }

    public void unfollowUser(UUID followerId, UUID followedId) {
        LOG.info("User " + followerId + " is unfollowing " + followedId);
        followRepository.deleteFollow(followerId, followedId);

        // Update Neo4j graph
        neo4jRepository.deleteFollow(followerId, followedId);

        // Publish event asynchronously
        redisMessagingService.publishFollowDeleted(followerId, followedId)
                .subscribe().with(
                        success -> LOG.debug("Unfollow event published successfully"),
                        failure -> LOG.error("Failed to publish unfollow event", failure));
    }

    public List<UUID> getUserFollowers(UUID userId) {
        // Use Neo4j for better performance with graph queries
        return neo4jRepository.getFollowers(userId);
    }

    public List<UUID> getUserFollowing(UUID userId) {
        // Use Neo4j for better performance with graph queries
        return neo4jRepository.getFollowing(userId);
    }

    // Block operations
    public BlockEntity blockUser(UUID blockerId, UUID blockedId) {
        LOG.info("User " + blockerId + " is blocking " + blockedId);
        BlockEntity block = new BlockEntity(blockerId, blockedId);
        blockRepository.persist(block);

        // Update Neo4j graph
        neo4jRepository.createBlock(blockerId, blockedId);

        // Publish block event
        BlocksUserContract blockContract = socialMapper.toBlocksUserContract(block);
        redisMessagingService.publishBlockCreated(blockContract)
                .subscribe().with(
                        success -> LOG.debug("Block event published successfully"),
                        failure -> LOG.error("Failed to publish block event", failure));

        // Asynchronously remove follow relationships
        deleteFollowsAsynchronously(blockerId, blockedId);

        return block;
    }

    public void unblockUser(UUID blockerId, UUID blockedId) {
        LOG.info("User " + blockerId + " is unblocking " + blockedId);
        blockRepository.deleteBlock(blockerId, blockedId);

        // Update Neo4j graph
        neo4jRepository.deleteBlock(blockerId, blockedId);

        // Publish unblock event
        redisMessagingService.publishBlockDeleted(blockerId, blockedId)
                .subscribe().with(
                        success -> LOG.debug("Unblock event published successfully"),
                        failure -> LOG.error("Failed to publish unblock event", failure));
    }

    public List<UUID> getUserBlocked(UUID userId) {
        // Use Neo4j for better performance with graph queries
        return neo4jRepository.getBlockedUsers(userId);
    }

    public List<UUID> getUserBlockers(UUID userId) {
        // Use Neo4j for better performance with graph queries
        return neo4jRepository.getBlockingUsers(userId);
    }

    // Social distance operations
    public int getSocialDistance(UUID user1Id, UUID user2Id) {
        return neo4jRepository.getSocialDistance(user1Id, user2Id);
    }

    public List<UUID> getUsersAtDistance(UUID userId, int distance) {
        return neo4jRepository.getUsersAtDistance(userId, distance);
    }

    // Helper methods
    private boolean hasBlockRelationshipBetweenUserAndPost(UUID userId, UUID postId) {
        // In a real implementation, you would need to get the post owner ID
        // This is a placeholder implementation assuming postId directly identifies the
        // user who owns the post
        // In a complete implementation, this would require calling the post service to
        // get the owner
        UUID postOwnerId = postId; // This needs to be replaced with actual implementation

        return blockRepository.hasBlockRelationship(userId, postOwnerId);
    }

    private void deleteFollowsAsynchronously(UUID user1, UUID user2) {
        // Run in a separate thread to make it asynchronous
        // In production, this would be better handled with a message queue processor
        new Thread(() -> {
            try {
                followRepository.deleteAllFollowsBetweenUsers(user1, user2);
                neo4jRepository.deleteFollow(user1, user2);
                neo4jRepository.deleteFollow(user2, user1);
                LOG.info("Successfully removed follow relationships between " + user1 + " and " + user2);
            } catch (Exception e) {
                LOG.error("Failed to remove follow relationships", e);
            }
        }).start();
    }
}
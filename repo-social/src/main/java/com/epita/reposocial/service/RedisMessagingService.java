package com.epita.reposocial.service;

import com.epita.contracts.BlocksUserContract;
import com.epita.contracts.FollowsContract;
import com.epita.contracts.LikesContract;
import io.quarkus.redis.client.RedisClient;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.jboss.logging.Logger;

import java.util.UUID;

@ApplicationScoped
public class RedisMessagingService {

    private static final Logger LOG = Logger.getLogger(RedisMessagingService.class);

    private static final String CHANNEL_LIKE_CREATED = "social.like.created";
    private static final String CHANNEL_LIKE_DELETED = "social.like.deleted";
    private static final String CHANNEL_FOLLOW_CREATED = "social.follow.created";
    private static final String CHANNEL_FOLLOW_DELETED = "social.follow.deleted";
    private static final String CHANNEL_BLOCK_CREATED = "social.block.created";
    private static final String CHANNEL_BLOCK_DELETED = "social.block.deleted";

    @Inject
    RedisClient redisClient;

    @Retry(maxRetries = 3, delay = 200)
    public Uni<Void> publishLikeCreated(LikesContract like) {
        LOG.debug("Publishing like created event: " + like.getUserId() + " liked " + like.getPostId());
        JsonObject message = JsonObject.mapFrom(like);
        return Uni.createFrom().voidItem()
                .invoke(() -> redisClient.publish(CHANNEL_LIKE_CREATED, message.encode()));
    }

    @Retry(maxRetries = 3, delay = 200)
    public Uni<Void> publishLikeDeleted(UUID userId, UUID postId) {
        LOG.debug("Publishing like deleted event: " + userId + " unliked " + postId);
        JsonObject message = new JsonObject()
                .put("userId", userId.toString())
                .put("postId", postId.toString());
        return Uni.createFrom().voidItem()
                .invoke(() -> redisClient.publish(CHANNEL_LIKE_DELETED, message.encode()));
    }

    @Retry(maxRetries = 3, delay = 200)
    public Uni<Void> publishFollowCreated(FollowsContract follow) {
        LOG.debug("Publishing follow created event: " + follow.getFollowerId() + " followed " + follow.getFolloweeId());
        JsonObject message = JsonObject.mapFrom(follow);
        return Uni.createFrom().voidItem()
                .invoke(() -> redisClient.publish(CHANNEL_FOLLOW_CREATED, message.encode()));
    }

    @Retry(maxRetries = 3, delay = 200)
    public Uni<Void> publishFollowDeleted(UUID followerId, UUID followedId) {
        LOG.debug("Publishing follow deleted event: " + followerId + " unfollowed " + followedId);
        JsonObject message = new JsonObject()
                .put("followerId", followerId.toString())
                .put("followedId", followedId.toString());
        return Uni.createFrom().voidItem()
                .invoke(() -> redisClient.publish(CHANNEL_FOLLOW_DELETED, message.encode()));
    }

    @Retry(maxRetries = 3, delay = 200)
    public Uni<Void> publishBlockCreated(BlocksUserContract block) {
        LOG.debug("Publishing block created event: " + block.getBlockerId() + " blocked " + block.getBlockedId());
        JsonObject message = JsonObject.mapFrom(block);
        return Uni.createFrom().voidItem()
                .invoke(() -> redisClient.publish(CHANNEL_BLOCK_CREATED, message.encode()));
    }

    @Retry(maxRetries = 3, delay = 200)
    public Uni<Void> publishBlockDeleted(UUID blockerId, UUID blockedId) {
        LOG.debug("Publishing block deleted event: " + blockerId + " unblocked " + blockedId);
        JsonObject message = new JsonObject()
                .put("blockerId", blockerId.toString())
                .put("blockedId", blockedId.toString());
        return Uni.createFrom().voidItem()
                .invoke(() -> redisClient.publish(CHANNEL_BLOCK_DELETED, message.encode()));
    }
}
package com.epita.reposocial.service;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.epita.contracts.BlocksUserContract;
import com.epita.contracts.FollowsContract;
import com.epita.contracts.LikesContract;
import io.quarkus.redis.client.RedisClient;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RedisMessagingServiceTest {

    @Mock
    RedisClient redisClient;

    @InjectMocks
    RedisMessagingService service;

    @BeforeEach
    void setup() {
        // Mock Redis publish method to return a value
        // Just return null - we're not actually using the return value in our tests
        when(redisClient.publish(anyString(), anyString())).thenReturn(null);
    }

    @Test
    void testPublishLikeCreated() {
        // Given
        LikesContract like = new LikesContract();
        like.setUserId(UUID.randomUUID());
        like.setPostId(UUID.randomUUID());
        like.setLikedAt(new Date(System.currentTimeMillis()).toInstant());

        // When
        UniAssertSubscriber<Void> subscriber = service.publishLikeCreated(like)
                .subscribe().withSubscriber(UniAssertSubscriber.create());

        // Then
        subscriber.assertCompleted();
        verify(redisClient).publish(anyString(), anyString());
    }

    @Test
    void testPublishLikeDeleted() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID postId = UUID.randomUUID();

        // When
        UniAssertSubscriber<Void> subscriber = service.publishLikeDeleted(userId, postId)
                .subscribe().withSubscriber(UniAssertSubscriber.create());

        // Then
        subscriber.assertCompleted();
        verify(redisClient).publish(anyString(), anyString());
    }

    @Test
    void testPublishFollowCreated() {
        // Given
        FollowsContract follow = new FollowsContract();
        follow.setFollowerId(UUID.randomUUID());
        follow.setFolloweeId(UUID.randomUUID());
        follow.setFollowedAt(new Date(System.currentTimeMillis()).toInstant());

        // When
        UniAssertSubscriber<Void> subscriber = service.publishFollowCreated(follow)
                .subscribe().withSubscriber(UniAssertSubscriber.create());

        // Then
        subscriber.assertCompleted();
        verify(redisClient).publish(anyString(), anyString());
    }

    @Test
    void testPublishFollowDeleted() {
        // Given
        UUID followerId = UUID.randomUUID();
        UUID followedId = UUID.randomUUID();

        // When
        UniAssertSubscriber<Void> subscriber = service.publishFollowDeleted(followerId, followedId)
                .subscribe().withSubscriber(UniAssertSubscriber.create());

        // Then
        subscriber.assertCompleted();
        verify(redisClient).publish(anyString(), anyString());
    }

    @Test
    void testPublishBlockCreated() {
        // Given
        BlocksUserContract block = new BlocksUserContract();
        block.setBlockerId(UUID.randomUUID());
        block.setBlockedId(UUID.randomUUID());
        block.setBlockedAt(new Date(System.currentTimeMillis()).toInstant());

        // When
        UniAssertSubscriber<Void> subscriber = service.publishBlockCreated(block)
                .subscribe().withSubscriber(UniAssertSubscriber.create());

        // Then
        subscriber.assertCompleted();
        verify(redisClient).publish(anyString(), anyString());
    }

    @Test
    void testPublishBlockDeleted() {
        // Given
        UUID blockerId = UUID.randomUUID();
        UUID blockedId = UUID.randomUUID();

        // When
        UniAssertSubscriber<Void> subscriber = service.publishBlockDeleted(blockerId, blockedId)
                .subscribe().withSubscriber(UniAssertSubscriber.create());

        // Then
        subscriber.assertCompleted();
        verify(redisClient).publish(anyString(), anyString());
    }
}
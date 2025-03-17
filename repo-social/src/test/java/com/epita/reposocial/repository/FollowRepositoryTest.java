package com.epita.reposocial.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.UUID;

import jakarta.inject.Inject;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.epita.reposocial.entity.FollowEntity;

import io.quarkus.test.junit.QuarkusTest;

/**
 * Tests for the {@link FollowRepositoryImpl} class.
 * 
 * Note: This is a repository test that would typically interact with a
 * database.
 * For a true unit test, you'd mock the Panache methods, but here we're showing
 * a more integration-style test. For real execution, you'd need MongoDB
 * running.
 */
@QuarkusTest
class FollowRepositoryTest {

    @Inject
    FollowRepositoryImpl repository;

    private UUID followerId;
    private UUID followedId;
    private FollowEntity followEntity;

    @BeforeEach
    void setup() {
        followerId = UUID.randomUUID();
        followedId = UUID.randomUUID();
        followEntity = new FollowEntity(followerId, followedId);
    }

    @AfterEach
    void cleanup() {
        repository.deleteAll();
    }

    @Test
    void testFindFollowersByUserId() {
        // Given
        repository.persist(followEntity);

        // When
        List<FollowEntity> followers = repository.findFollowersByUserId(followedId);

        // Then
        assertEquals(1, followers.size());
        assertEquals(followerId, followers.get(0).followerId);
        assertEquals(followedId, followers.get(0).followedId);
    }

    @Test
    void testFindFollowedByUserId() {
        // Given
        repository.persist(followEntity);

        // When
        List<FollowEntity> following = repository.findFollowedByUserId(followerId);

        // Then
        assertEquals(1, following.size());
        assertEquals(followerId, following.get(0).followerId);
        assertEquals(followedId, following.get(0).followedId);
    }

    @Test
    void testDeleteFollow() {
        // Given
        repository.persist(followEntity);
        assertTrue(repository.exists(followerId, followedId));

        // When
        repository.deleteFollow(followerId, followedId);

        // Then
        assertFalse(repository.exists(followerId, followedId));
    }

    @Test
    void testExists() {
        // Given
        repository.persist(followEntity);

        // When & Then
        assertTrue(repository.exists(followerId, followedId));
        assertFalse(repository.exists(UUID.randomUUID(), followedId)); // Different followerId
        assertFalse(repository.exists(followerId, UUID.randomUUID())); // Different followedId
    }

    @Test
    void testDeleteAllFollowsBetweenUsers() {
        // Given
        UUID user1 = UUID.randomUUID();
        UUID user2 = UUID.randomUUID();

        repository.persist(new FollowEntity(user1, user2)); // user1 follows user2
        repository.persist(new FollowEntity(user2, user1)); // user2 follows user1

        assertTrue(repository.exists(user1, user2));
        assertTrue(repository.exists(user2, user1));

        // When
        repository.deleteAllFollowsBetweenUsers(user1, user2);

        // Then
        assertFalse(repository.exists(user1, user2));
        assertFalse(repository.exists(user2, user1));
    }
}
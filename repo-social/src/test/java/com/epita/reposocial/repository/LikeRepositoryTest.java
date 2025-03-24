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

import com.epita.reposocial.entity.LikeEntity;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;

/**
 * Tests for the {@link LikeRepositoryImpl} class.
 * 
 * Note: This is a repository test that would typically interact with a
 * database.
 * For a true unit test, you'd mock the Panache methods, but here we're showing
 * a more integration-style test. For real execution, you'd need MongoDB
 * running.
 */
@QuarkusTest
class LikeRepositoryTest {

    @Inject
    LikeRepositoryImpl repository;

    private UUID userId;
    private UUID postId;
    private LikeEntity likeEntity;

    @BeforeEach
    void setup() {
        userId = UUID.randomUUID();
        postId = UUID.randomUUID();
        likeEntity = new LikeEntity(userId, postId);
    }

    @AfterEach
    void cleanup() {
        repository.deleteAll();
    }

    @Test
    void testFindByUserId() {
        // Given
        repository.persist(likeEntity);

        // When
        List<LikeEntity> likes = repository.findByUserId(userId);

        // Then
        assertEquals(1, likes.size());
        assertEquals(userId.toString(), likes.get(0).userId);
        assertEquals(postId.toString(), likes.get(0).postId);
    }

    @Test
    void testFindByPostId() {
        // Given
        repository.persist(likeEntity);

        // When
        List<LikeEntity> likes = repository.findByPostId(postId);

        // Then
        assertEquals(1, likes.size());
        assertEquals(userId.toString(), likes.get(0).userId);
        assertEquals(postId.toString(), likes.get(0).postId);
    }

    @Test
    void testDeleteLike() {
        // Given
        repository.persist(likeEntity);
        assertTrue(repository.exists(userId, postId));

        // When
        repository.deleteLike(userId, postId);

        // Then
        assertFalse(repository.exists(userId, postId));
    }

    @Test
    void testExists() {
        // Given
        repository.persist(likeEntity);

        // When & Then
        assertTrue(repository.exists(userId, postId));
        assertFalse(repository.exists(UUID.randomUUID(), postId)); // Different userId
        assertFalse(repository.exists(userId, UUID.randomUUID())); // Different postId
    }
}
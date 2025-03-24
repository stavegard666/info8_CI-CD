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

import com.epita.reposocial.entity.BlockEntity;

import io.quarkus.test.junit.QuarkusTest;

/**
 * Tests for the {@link BlockRepositoryImpl} class.
 * 
 * Note: This is a repository test that would typically interact with a
 * database.
 * For a true unit test, you'd mock the Panache methods, but here we're showing
 * a more integration-style test. For real execution, you'd need MongoDB
 * running.
 */
@QuarkusTest
class BlockRepositoryTest {

    @Inject
    BlockRepositoryImpl repository;

    private UUID blockerId;
    private UUID blockedId;
    private BlockEntity blockEntity;

    @BeforeEach
    void setup() {
        blockerId = UUID.randomUUID();
        blockedId = UUID.randomUUID();
        blockEntity = new BlockEntity(blockerId, blockedId);
    }

    @AfterEach
    void cleanup() {
        repository.deleteAll();
    }

    @Test
    void testFindBlockersByUserId() {
        // Given
        repository.persist(blockEntity);

        // When
        List<BlockEntity> blockers = repository.findBlockersByUserId(blockedId);

        // Then
        assertEquals(1, blockers.size());
        assertEquals(blockerId.toString(), blockers.get(0).blockerId);
        assertEquals(blockedId.toString(), blockers.get(0).blockedId);
    }

    @Test
    void testFindBlockedByUserId() {
        // Given
        repository.persist(blockEntity);

        // When
        List<BlockEntity> blocked = repository.findBlockedByUserId(blockerId);

        // Then
        assertEquals(1, blocked.size());
        assertEquals(blockerId.toString(), blocked.get(0).blockerId);
        assertEquals(blockedId.toString(), blocked.get(0).blockedId);
    }

    @Test
    void testDeleteBlock() {
        // Given
        repository.persist(blockEntity);
        assertTrue(repository.exists(blockerId, blockedId));

        // When
        repository.deleteBlock(blockerId, blockedId);

        // Then
        assertFalse(repository.exists(blockerId, blockedId));
    }

    @Test
    void testExists() {
        // Given
        repository.persist(blockEntity);

        // When & Then
        assertTrue(repository.exists(blockerId, blockedId));
        assertFalse(repository.exists(UUID.randomUUID(), blockedId)); // Different blockerId
        assertFalse(repository.exists(blockerId, UUID.randomUUID())); // Different blockedId
    }

    @Test
    void testHasBlockRelationship() {
        // Given
        UUID user1 = UUID.randomUUID();
        UUID user2 = UUID.randomUUID();

        repository.persist(new BlockEntity(user1, user2)); // user1 blocks user2

        // When & Then
        assertTrue(repository.hasBlockRelationship(user1, user2));
        assertTrue(repository.hasBlockRelationship(user2, user1)); // Should work in both directions
        assertFalse(repository.hasBlockRelationship(user1, UUID.randomUUID())); // Different user
    }
}
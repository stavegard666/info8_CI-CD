package com.epita.reposocial.repository;

import com.epita.reposocial.entity.BlockEntity;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class BlockRepositoryImpl implements PanacheMongoRepository<BlockEntity> {

    public List<BlockEntity> findBlockersByUserId(UUID userId) {
        return list("blockedId", userId);
    }

    public List<BlockEntity> findBlockedByUserId(UUID userId) {
        return list("blockerId", userId);
    }

    public void deleteBlock(UUID blockerId, UUID blockedId) {
        delete("blockerId = ?1 and blockedId = ?2", blockerId, blockedId);
    }

    public boolean exists(UUID blockerId, UUID blockedId) {
        return count("blockerId = ?1 and blockedId = ?2", blockerId, blockedId) > 0;
    }

    public boolean hasBlockRelationship(UUID user1, UUID user2) {
        return count("(blockerId = ?1 and blockedId = ?2) or (blockerId = ?2 and blockedId = ?1)",
                user1, user2) > 0;
    }
}
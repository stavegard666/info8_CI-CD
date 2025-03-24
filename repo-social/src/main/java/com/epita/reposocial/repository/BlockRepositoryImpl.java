package com.epita.reposocial.repository;

import com.epita.reposocial.entity.BlockEntity;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class BlockRepositoryImpl implements PanacheMongoRepository<BlockEntity> {

    public List<BlockEntity> findBlockersByUserId(UUID userId) {
        return list("blockedId", userId.toString());
    }

    public List<BlockEntity> findBlockedByUserId(UUID userId) {
        return list("blockerId", userId.toString());
    }

    public void deleteBlock(UUID blockerId, UUID blockedId) {
        delete("blockerId = ?1 and blockedId = ?2", blockerId.toString(), blockedId.toString());
    }

    public boolean exists(UUID blockerId, UUID blockedId) {
        return count("blockerId = ?1 and blockedId = ?2", blockerId.toString(), blockedId.toString()) > 0;
    }

    public boolean hasBlockRelationship(UUID user1, UUID user2) {
        return exists(user1, user2) || exists(user2, user1);
    }
}
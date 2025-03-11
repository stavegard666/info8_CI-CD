package com.epita.reposocial.repository;

import com.epita.reposocial.entity.BlockEntity;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class BlockRepositoryImpl implements PanacheMongoRepository<BlockEntity> {
    
    public List<BlockEntity> findBlockersByUserId(String userId) {
        return list("blockedId", userId);
    }

    public List<BlockEntity> findBlockedByUserId(String userId) {
        return list("blockerId", userId);
    }

    public void deleteBlock(String blockerId, String blockedId) {
        delete("blockerId = ?1 and blockedId = ?2", blockerId, blockedId);
    }

    public boolean exists(String blockerId, String blockedId) {
        return count("blockerId = ?1 and blockedId = ?2", blockerId, blockedId) > 0;
    }

    public boolean hasBlockRelationship(String user1, String user2) {
        return count("(blockerId = ?1 and blockedId = ?2) or (blockerId = ?2 and blockedId = ?1)", 
                    user1, user2) > 0;
    }
} 
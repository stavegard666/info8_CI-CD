package com.epita.reposocial.entity;

import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.types.ObjectId;

import java.time.Instant;

@MongoEntity(collection = "blocks")
public class BlockEntity {
    public ObjectId id;
    public String blockerId;
    public String blockedId;
    public Instant blockedAt;

    public BlockEntity() {
    }

    public BlockEntity(String blockerId, String blockedId) {
        this.blockerId = blockerId;
        this.blockedId = blockedId;
        this.blockedAt = Instant.now();
    }
} 
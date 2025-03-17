package com.epita.reposocial.entity;

import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.types.ObjectId;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@MongoEntity(collection = "blocks")
public class BlockEntity {
    public ObjectId id;
    public UUID blockerId;
    public UUID blockedId;
    public Date blockedAt;

    public BlockEntity() {
    }

    public BlockEntity(UUID blockerId, UUID blockedId) {
        this.blockerId = blockerId;
        this.blockedId = blockedId;
        this.blockedAt = new Date();
    }
}
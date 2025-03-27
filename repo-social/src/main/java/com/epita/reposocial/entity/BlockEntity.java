package com.epita.reposocial.entity;

import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.Date;
import java.util.UUID;

@MongoEntity(collection = "blocks")
public class BlockEntity {
    public String blockerId;
    public String blockedId;
    public Date blockedAt;

    public BlockEntity() {
    }

    public BlockEntity(UUID blockerId, UUID blockedId) {
        this.blockerId = blockerId.toString();
        this.blockedId = blockedId.toString();
        this.blockedAt = new Date();
    }
}
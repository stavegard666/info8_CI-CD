package com.epita.reposocial.entity;

import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@MongoEntity(collection = "blocks")
public class BlockEntity {
    @BsonId
    public ObjectId id;

    @BsonProperty("blockerId")
    public UUID blockerId;

    @BsonProperty("blockedId")
    public UUID blockedId;

    @BsonProperty("blockedAt")
    public Date blockedAt;

    public BlockEntity() {
    }

    public BlockEntity(UUID blockerId, UUID blockedId) {
        this.blockerId = blockerId;
        this.blockedId = blockedId;
        this.blockedAt = new Date();
    }
}
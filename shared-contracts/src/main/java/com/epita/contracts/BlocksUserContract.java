package com.epita.contracts;

import java.time.Instant;
import java.util.UUID;

import org.bson.codecs.pojo.annotations.BsonProperty;

import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection="blocks")
public class BlocksUserContract {

    @BsonProperty("_id")
    private UUID id;
    private UUID blockerId;
    private UUID blockedId;
    private Instant blockedAt;

    public BlocksUserContract() {
    }

    public BlocksUserContract(UUID blockerId, UUID blockedId, Instant blockedAt) {
        this.blockerId = blockerId;
        this.blockedId = blockedId;
        this.blockedAt = blockedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getBlockerId() {
        return blockerId;
    }

    public void setBlockerId(UUID blockerId) {
        this.blockerId = blockerId;
    }

    public UUID getBlockedId() {
        return blockedId;
    }

    public void setBlockedId(UUID blockedId) {
        this.blockedId = blockedId;
    }

    public Instant getBlockedAt() {
        return blockedAt;
    }

    public void setBlockedAt(Instant blockedAt) {
        this.blockedAt = blockedAt;
    }
}

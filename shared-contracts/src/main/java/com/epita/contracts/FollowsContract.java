package com.epita.contracts;

import java.time.Instant;
import java.util.UUID;

import org.bson.codecs.pojo.annotations.BsonProperty;

import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection="follows")
public class FollowsContract {

    @BsonProperty("_id")
    private UUID id;
    private UUID followerId;
    private UUID followeeId;
    private Instant followedAt;

    public FollowsContract() {
    }

    public FollowsContract(UUID followerId, UUID followeeId, Instant followedAt) {
        this.followerId = followerId;
        this.followeeId = followeeId;
        this.followedAt = followedAt;
    }

    public UUID getFollowerId() {
        return followerId;
    }

    public void setFollowerId(UUID followerId) {
        this.followerId = followerId;
    }

    public UUID getFolloweeId() {
        return followeeId;
    }

    public void setFolloweeId(UUID followeeId) {
        this.followeeId = followeeId;
    }

    public Instant getFollowedAt() {
        return followedAt;
    }

    public void setFollowedAt(Instant followedAt) {
        this.followedAt = followedAt;
    }
}

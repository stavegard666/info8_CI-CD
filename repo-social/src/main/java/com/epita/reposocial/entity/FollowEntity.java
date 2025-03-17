package com.epita.reposocial.entity;

import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.types.ObjectId;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@MongoEntity(collection = "follows")
public class FollowEntity {
    public ObjectId id;
    public UUID followerId;
    public UUID followedId;
    public Date followedAt;

    public FollowEntity() {
    }

    public FollowEntity(UUID followerId, UUID followedId) {
        this.followerId = followerId;
        this.followedId = followedId;
        this.followedAt = new Date();
    }
}
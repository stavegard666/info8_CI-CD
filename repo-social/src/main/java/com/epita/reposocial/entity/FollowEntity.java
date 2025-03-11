package com.epita.reposocial.entity;

import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.types.ObjectId;

import java.time.Instant;

@MongoEntity(collection = "follows")
public class FollowEntity {
    public ObjectId id;
    public String followerId;
    public String followedId;
    public Instant followedAt;

    public FollowEntity() {
    }

    public FollowEntity(String followerId, String followedId) {
        this.followerId = followerId;
        this.followedId = followedId;
        this.followedAt = Instant.now();
    }
} 
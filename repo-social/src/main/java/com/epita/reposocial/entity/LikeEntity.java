package com.epita.reposocial.entity;

import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.types.ObjectId;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@MongoEntity(collection = "likes")
public class LikeEntity {
    public ObjectId id;
    public UUID userId;
    public UUID postId;
    public Date likedAt;

    public LikeEntity() {
    }

    public LikeEntity(UUID userId, UUID postId) {
        this.userId = userId;
        this.postId = postId;
        this.likedAt = new Date();
    }
}
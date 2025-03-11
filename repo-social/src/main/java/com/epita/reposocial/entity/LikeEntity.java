package com.epita.reposocial.entity;

import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.types.ObjectId;

import java.time.Instant;

@MongoEntity(collection = "likes")
public class LikeEntity {
    public ObjectId id;
    public String userId;
    public String postId;
    public Instant likedAt;

    public LikeEntity() {
    }

    public LikeEntity(String userId, String postId) {
        this.userId = userId;
        this.postId = postId;
        this.likedAt = Instant.now();
    }
} 
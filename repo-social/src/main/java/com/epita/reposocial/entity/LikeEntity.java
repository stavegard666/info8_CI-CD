package com.epita.reposocial.entity;

import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.Date;
import java.util.UUID;

@MongoEntity(collection = "likes")
public class LikeEntity {
    public String userId;
    public String postId;
    public Date likedAt;

    public LikeEntity() {
    }

    public LikeEntity(UUID userId, UUID postId) {
        this.userId = userId.toString();
        this.postId = postId.toString();
        this.likedAt = new Date();
    }
}
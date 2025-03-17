package com.epita.reposocial.entity;

import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@MongoEntity(collection = "likes")
public class LikeEntity {
    @BsonId
    public ObjectId id;

    @BsonProperty("userId")
    public UUID userId;

    @BsonProperty("postId")
    public UUID postId;

    @BsonProperty("likedAt")
    public Date likedAt;

    public LikeEntity() {
    }

    public LikeEntity(UUID userId, UUID postId) {
        this.userId = userId;
        this.postId = postId;
        this.likedAt = new Date();
    }
}
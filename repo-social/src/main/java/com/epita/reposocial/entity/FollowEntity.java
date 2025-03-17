package com.epita.reposocial.entity;

import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@MongoEntity(collection = "follows")
public class FollowEntity {
    @BsonId
    public ObjectId id;

    @BsonProperty("followerId")
    public UUID followerId;

    @BsonProperty("followedId")
    public UUID followedId;

    @BsonProperty("followedAt")
    public Date followedAt;

    public FollowEntity() {
    }

    public FollowEntity(UUID followerId, UUID followedId) {
        this.followerId = followerId;
        this.followedId = followedId;
        this.followedAt = new Date();
    }
}
package com.epita.reposocial.entity;

import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.Date;
import java.util.UUID;

@MongoEntity(collection = "follows")
public class FollowEntity {
    @BsonProperty("_id")
    public String followerId;
    public String followedId;
    public Date followedAt;

    public FollowEntity() {
    }

    public FollowEntity(UUID followerId, UUID followedId) {
        this.followerId = followerId.toString();
        this.followedId = followedId.toString();
        this.followedAt = new Date();
    }
}
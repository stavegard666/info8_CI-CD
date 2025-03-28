package com.epita.contracts;

import java.time.Instant;
import java.util.UUID;

import org.bson.codecs.pojo.annotations.BsonProperty;

public class LikesContract {

    @BsonProperty("_id")
    private UUID id;
    private UUID userId;
    private UUID postId;
    private Instant likedAt;

    public LikesContract() {
    }

    public LikesContract(UUID userId, UUID postId, Instant likedAt) {
        this.userId = userId;
        this.postId = postId;
        this.likedAt = likedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getPostId() {
        return postId;
    }

    public void setPostId(UUID postId) {
        this.postId = postId;
    }

    public Instant getLikedAt() {
        return likedAt;
    }

    public void setLikedAt(Instant likedAt) {
        this.likedAt = likedAt;
    }
}

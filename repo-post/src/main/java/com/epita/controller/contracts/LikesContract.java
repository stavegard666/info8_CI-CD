package com.epita.controller.contracts;

import java.sql.Date;
import java.util.UUID;

public class LikesContract {
    private UUID userId;
    private UUID postId;
    private Date likedAt;

    public LikesContract() {
    }

    public LikesContract(UUID userId, UUID postId, Date likedAt) {
        this.userId = userId;
        this.postId = postId;
        this.likedAt = likedAt;
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

    public Date getLikedAt() {
        return likedAt;
    }

    public void setLikedAt(Date likedAt) {
        this.likedAt = likedAt;
    }
}

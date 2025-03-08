package com.epita.contracts;

import java.sql.Date;
import java.util.UUID;

public class FollowsContract {
    private UUID followerId;
    private UUID followeeId;
    private Date followedAt;

    public FollowsContract() {
    }

    public FollowsContract(UUID followerId, UUID followeeId, Date followedAt) {
        this.followerId = followerId;
        this.followeeId = followeeId;
        this.followedAt = followedAt;
    }

    public UUID getFollowerId() {
        return followerId;
    }

    public void setFollowerId(UUID followerId) {
        this.followerId = followerId;
    }

    public UUID getFolloweeId() {
        return followeeId;
    }

    public void setFolloweeId(UUID followeeId) {
        this.followeeId = followeeId;
    }

    public Date getFollowedAt() {
        return followedAt;
    }

    public void setFollowedAt(Date followedAt) {
        this.followedAt = followedAt;
    }
}

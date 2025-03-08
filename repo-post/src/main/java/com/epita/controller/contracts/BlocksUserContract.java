package com.epita.controller.contracts;

import java.util.Date;
import java.util.UUID;

public class BlocksUserContract {
    private UUID blockerId;
    private UUID blockedId;
    private Date blockedAt;

    public BlocksUserContract() {
    }

    public BlocksUserContract(UUID blockerId, UUID blockedId, Date blockedAt) {
        this.blockerId = blockerId;
        this.blockedId = blockedId;
        this.blockedAt = blockedAt;
    }

    public UUID getBlockerId() {
        return blockerId;
    }

    public void setBlockerId(UUID blockerId) {
        this.blockerId = blockerId;
    }

    public UUID getBlockedId() {
        return blockedId;
    }

    public void setBlockedId(UUID blockedId) {
        this.blockedId = blockedId;
    }

    public Date getBlockedAt() {
        return blockedAt;
    }

    public void setBlockedAt(Date blockedAt) {
        this.blockedAt = blockedAt;
    }
}

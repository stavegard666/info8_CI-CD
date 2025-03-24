package com.epita.reposocial.mapper;

import com.epita.contracts.BlocksUserContract;
import com.epita.contracts.FollowsContract;
import com.epita.contracts.LikesContract;
import com.epita.reposocial.entity.BlockEntity;
import com.epita.reposocial.entity.FollowEntity;
import com.epita.reposocial.entity.LikeEntity;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Date;
import java.util.UUID;

@ApplicationScoped
public class SocialMapper {

    public LikesContract toLikesContract(LikeEntity entity) {
        LikesContract contract = new LikesContract();
        contract.setUserId(UUID.fromString(entity.userId));
        contract.setPostId(UUID.fromString(entity.postId));
        contract.setLikedAt(new Date(entity.likedAt.getTime()).toInstant());
        return contract;
    }

    public FollowsContract toFollowsContract(FollowEntity entity) {
        FollowsContract contract = new FollowsContract();
        contract.setFollowerId(UUID.fromString(entity.followerId));
        contract.setFolloweeId(UUID.fromString(entity.followedId));
        contract.setFollowedAt(new Date(entity.followedAt.getTime()).toInstant());
        return contract;
    }

    public BlocksUserContract toBlocksUserContract(BlockEntity entity) {
        BlocksUserContract contract = new BlocksUserContract();
        contract.setBlockerId(UUID.fromString(entity.blockerId));
        contract.setBlockedId(UUID.fromString(entity.blockedId));
        contract.setBlockedAt(new Date(entity.blockedAt.getTime()).toInstant());
        return contract;
    }
}
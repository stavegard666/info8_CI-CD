package com.epita.reposocial.mapper;

import com.epita.contracts.BlocksUserContract;
import com.epita.contracts.FollowsContract;
import com.epita.contracts.LikesContract;
import com.epita.reposocial.entity.BlockEntity;
import com.epita.reposocial.entity.FollowEntity;
import com.epita.reposocial.entity.LikeEntity;
import jakarta.enterprise.context.ApplicationScoped;

import java.sql.Date;

@ApplicationScoped
public class SocialMapper {

    public LikesContract toLikesContract(LikeEntity entity) {
        LikesContract contract = new LikesContract();
        contract.setUserId(entity.userId);
        contract.setPostId(entity.postId);
        contract.setLikedAt(new Date(entity.likedAt.getTime()));
        return contract;
    }

    public FollowsContract toFollowsContract(FollowEntity entity) {
        FollowsContract contract = new FollowsContract();
        contract.setFollowerId(entity.followerId);
        contract.setFolloweeId(entity.followedId);
        contract.setFollowedAt(new Date(entity.followedAt.getTime()));
        return contract;
    }

    public BlocksUserContract toBlocksUserContract(BlockEntity entity) {
        BlocksUserContract contract = new BlocksUserContract();
        contract.setBlockerId(entity.blockerId);
        contract.setBlockedId(entity.blockedId);
        contract.setBlockedAt(new Date(entity.blockedAt.getTime()));
        return contract;
    }
}
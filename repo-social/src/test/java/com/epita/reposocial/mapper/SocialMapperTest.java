package com.epita.reposocial.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.epita.contracts.BlocksUserContract;
import com.epita.contracts.FollowsContract;
import com.epita.contracts.LikesContract;
import com.epita.reposocial.entity.BlockEntity;
import com.epita.reposocial.entity.FollowEntity;
import com.epita.reposocial.entity.LikeEntity;

class SocialMapperTest {

    private SocialMapper mapper;

    @BeforeEach
    void setup() {
        mapper = new SocialMapper();
    }

    @Test
    void testLikesContractMapping() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID postId = UUID.randomUUID();
        LikeEntity entity = new LikeEntity(userId, postId);

        // When
        LikesContract contract = mapper.toLikesContract(entity);

        // Then
        assertNotNull(contract);
        assertEquals(userId, contract.getUserId());
        assertEquals(postId, contract.getPostId());
        assertNotNull(contract.getLikedAt());
    }

    @Test
    void testFollowsContractMapping() {
        // Given
        UUID followerId = UUID.randomUUID();
        UUID followedId = UUID.randomUUID();
        FollowEntity entity = new FollowEntity(followerId, followedId);

        // When
        FollowsContract contract = mapper.toFollowsContract(entity);

        // Then
        assertNotNull(contract);
        assertEquals(followerId, contract.getFollowerId());
        assertEquals(followedId, contract.getFolloweeId());
        assertNotNull(contract.getFollowedAt());
    }

    @Test
    void testBlocksUserContractMapping() {
        // Given
        UUID blockerId = UUID.randomUUID();
        UUID blockedId = UUID.randomUUID();
        BlockEntity entity = new BlockEntity(blockerId, blockedId);

        // When
        BlocksUserContract contract = mapper.toBlocksUserContract(entity);

        // Then
        assertNotNull(contract);
        assertEquals(blockerId, contract.getBlockerId());
        assertEquals(blockedId, contract.getBlockedId());
        assertNotNull(contract.getBlockedAt());
    }
}
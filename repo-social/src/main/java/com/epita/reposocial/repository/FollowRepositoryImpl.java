package com.epita.reposocial.repository;

import com.epita.reposocial.entity.FollowEntity;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class FollowRepositoryImpl implements PanacheMongoRepository<FollowEntity> {

    public List<FollowEntity> findFollowersByUserId(UUID userId) {
        return list("followedId", userId);
    }

    public List<FollowEntity> findFollowedByUserId(UUID userId) {
        return list("followerId", userId);
    }

    public void deleteFollow(UUID followerId, UUID followedId) {
        delete("followerId = ?1 and followedId = ?2", followerId, followedId);
    }

    public boolean exists(UUID followerId, UUID followedId) {
        return count("followerId = ?1 and followedId = ?2", followerId, followedId) > 0;
    }

    public void deleteAllFollowsBetweenUsers(UUID user1, UUID user2) {
        delete("(followerId = ?1 and followedId = ?2) or (followerId = ?2 and followedId = ?1)",
                user1, user2);
    }
}
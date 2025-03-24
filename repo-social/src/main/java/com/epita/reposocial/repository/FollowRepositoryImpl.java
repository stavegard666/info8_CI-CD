package com.epita.reposocial.repository;

import com.epita.reposocial.entity.FollowEntity;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class FollowRepositoryImpl implements PanacheMongoRepository<FollowEntity> {

    public List<FollowEntity> findFollowersByUserId(UUID userId) {
        return list("followedId", userId.toString());
    }

    public List<FollowEntity> findFollowedByUserId(UUID userId) {
        return list("followerId", userId.toString());
    }

    public void deleteFollow(UUID followerId, UUID followedId) {
        delete("followerId = ?1 and followedId = ?2", followerId.toString(), followedId.toString());
    }

    public boolean exists(UUID followerId, UUID followedId) {
        return count("followerId = ?1 and followedId = ?2", followerId.toString(), followedId.toString()) > 0;
    }

    public void deleteAllFollowsBetweenUsers(UUID user1, UUID user2) {
        delete("followerId = ?1 and followedId = ?2", user1.toString(), user2.toString());
        delete("followerId = ?2 and followedId = ?1", user1.toString(), user2.toString());
    }
}
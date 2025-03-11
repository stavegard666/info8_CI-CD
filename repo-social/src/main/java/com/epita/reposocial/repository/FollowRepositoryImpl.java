package com.epita.reposocial.repository;

import com.epita.reposocial.entity.FollowEntity;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class FollowRepositoryImpl implements PanacheMongoRepository<FollowEntity> {
    
    public List<FollowEntity> findFollowersByUserId(String userId) {
        return list("followedId", userId);
    }

    public List<FollowEntity> findFollowedByUserId(String userId) {
        return list("followerId", userId);
    }

    public void deleteFollow(String followerId, String followedId) {
        delete("followerId = ?1 and followedId = ?2", followerId, followedId);
    }

    public boolean exists(String followerId, String followedId) {
        return count("followerId = ?1 and followedId = ?2", followerId, followedId) > 0;
    }

    public void deleteAllFollowsBetweenUsers(String user1, String user2) {
        delete("(followerId = ?1 and followedId = ?2) or (followerId = ?2 and followedId = ?1)", 
              user1, user2);
    }
} 
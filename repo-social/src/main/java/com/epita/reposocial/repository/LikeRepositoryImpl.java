package com.epita.reposocial.repository;

import com.epita.reposocial.entity.LikeEntity;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class LikeRepositoryImpl implements PanacheMongoRepository<LikeEntity> {
    
    public List<LikeEntity> findByUserId(String userId) {
        return list("userId", userId);
    }

    public List<LikeEntity> findByPostId(String postId) {
        return list("postId", postId);
    }

    public void deleteLike(String userId, String postId) {
        delete("userId = ?1 and postId = ?2", userId, postId);
    }

    public boolean exists(String userId, String postId) {
        return count("userId = ?1 and postId = ?2", userId, postId) > 0;
    }
} 
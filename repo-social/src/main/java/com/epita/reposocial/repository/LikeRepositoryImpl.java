package com.epita.reposocial.repository;

import com.epita.reposocial.entity.LikeEntity;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class LikeRepositoryImpl implements PanacheMongoRepository<LikeEntity> {

    public List<LikeEntity> findByUserId(UUID userId) {
        return list("userId", userId.toString());
    }

    public List<LikeEntity> findByPostId(UUID postId) {
        return list("postId", postId.toString());
    }

    public void deleteLike(UUID userId, UUID postId) {
        delete("userId = ?1 and postId = ?2", userId.toString(), postId.toString());
    }

    public boolean exists(UUID userId, UUID postId) {
        return count("userId = ?1 and postId = ?2", userId.toString(), postId.toString()) > 0;
    }
}
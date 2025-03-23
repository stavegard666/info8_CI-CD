package com.epita.repository;

import com.epita.repository.entity.UserEntity;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class UserRepository implements PanacheMongoRepository<UserEntity> {

    public Optional<UserEntity> findUserById(UUID userId) {
        return find("userId", userId).firstResultOptional();
    }
}

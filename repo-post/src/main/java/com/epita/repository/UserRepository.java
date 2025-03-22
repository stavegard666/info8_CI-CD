package com.epita.repository;

import com.epita.contracts.UsersContract;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class UserRepository implements PanacheMongoRepository<UsersContract> {

    public Optional<UsersContract> findUserById(UUID userId) {
        return find("userId", userId).firstResultOptional();
    }
}

package com.epita.repository;

import com.epita.contracts.BlocksUserContract;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class BlocksUserRepostiory implements PanacheMongoRepository<BlocksUserContract> {

    public Optional<BlocksUserContract> findUserById(UUID userId, UUID blockedId) {
        return find("blockerId = ?1 and blockedId = ?2", userId, blockedId).firstResultOptional();
    }
}

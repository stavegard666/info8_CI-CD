package com.epita.repository;

import com.epita.contracts.UsersContract;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheMongoRepository<UsersContract> {
    
}

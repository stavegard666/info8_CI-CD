package com.epita.repository;

import com.epita.repository.entity.UsersEntity;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheMongoRepository<UsersEntity> {
    
}

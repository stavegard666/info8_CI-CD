package com.epita.repository;

import com.epita.contracts.UserTimelineContract;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserTimeLineRepository implements PanacheMongoRepository<UserTimelineContract> {
    
}

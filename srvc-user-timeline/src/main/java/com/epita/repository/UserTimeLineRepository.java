package com.epita.repository;

import com.epita.repository.entity.UserTimelineEntity;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserTimeLineRepository implements PanacheMongoRepository<UserTimelineEntity> {
    
}

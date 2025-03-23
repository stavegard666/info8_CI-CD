package com.epita.repository.entity;

import java.time.Instant;
import java.util.UUID;

import org.bson.codecs.pojo.annotations.BsonProperty;

import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.Data;

@MongoEntity(collection="users")
@Data
public class UsersEntity {
    
    @BsonProperty("_id")
    private UUID userId;
    private String userName;
    private Instant birthDate;
    private String location;
}

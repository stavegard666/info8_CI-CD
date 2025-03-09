package com.epita.contracts;

import java.time.Instant;
import java.util.UUID;

import org.bson.codecs.pojo.annotations.BsonProperty;

import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection="users")
public class UsersContract {
    
    @BsonProperty("_id")
    private UUID userId;
    private String userName;
    private Instant birthDate;
    private String location;

    public UsersContract() {
    }

    public UsersContract(UUID userId, String userName, Instant birthDate, String location) {
        this.userId = userId;
        this.userName = userName;
        this.birthDate = birthDate;
        this.location = location;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Instant getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Instant birthDate) {
        this.birthDate = birthDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

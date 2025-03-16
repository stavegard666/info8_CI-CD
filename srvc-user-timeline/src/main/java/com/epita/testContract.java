package com.epita;

import java.util.UUID;

import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection="zeub")
public class testContract {
    public UUID postId;
    UUID userId;
    String content;
}

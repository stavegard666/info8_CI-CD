package com.epita.repository.entity;

import java.time.Instant;
import java.util.UUID;

import org.bson.codecs.pojo.annotations.BsonProperty;

import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.Data;

@MongoEntity(collection="likes")
@Data
public class LikesEntity {
    
    @BsonProperty("_id")
    private UUID id;
    private UUID userId;
    private UUID postId;
    private Instant likedAt;



    public com.epita.contracts.LikesContract toContract() {
        return new com.epita.contracts.LikesContract(userId, postId, likedAt);
    }
}

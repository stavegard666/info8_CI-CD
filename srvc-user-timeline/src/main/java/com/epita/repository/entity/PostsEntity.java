package com.epita.repository.entity;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.bson.codecs.pojo.annotations.BsonProperty;

import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.Data;

@MongoEntity(collection="posts")
@Data
public class PostsEntity {
    
    @BsonProperty("_id")
    private UUID postId;
    private UUID userId;
    private String content;
    private List<String> hashtags;
    private String mediaUrl;
    private UUID repostOf;
    private UUID replyTo;
    private Instant createdAt;


    public com.epita.contracts.PostsContract toContract() {
        return new com.epita.contracts.PostsContract(postId, userId, content, hashtags, mediaUrl, repostOf, replyTo, createdAt);
    }
}

package com.epita.repository.entity;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.bson.codecs.pojo.annotations.BsonProperty;

import com.epita.contracts.PostsContract;

import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
@MongoEntity(collection = "posts")
public class PostsEntity extends PanacheMongoEntityBase {
    
    @BsonProperty("_id")
    private UUID postId;
    private UUID userId;
    private String content;
    private List<String> hashtags;
    private String mediaUrl;
    private UUID repostOf;
    private UUID replyTo;
    private Instant createdAt;

    public PostsContract toContract() {
        return new PostsContract(postId, userId, content, hashtags, mediaUrl, repostOf, replyTo, createdAt);
    }
    public PostsEntity(PostsContract post) {
        this.postId = UUID.randomUUID();
        this.userId = post.getUserId();
        this.content = post.getContent();
        this.hashtags = post.getHashtags();
        this.mediaUrl = post.getMediaUrl();
        this.repostOf = post.getRepostOf();
        this.replyTo = post.getReplyTo();
        this.createdAt = Instant.now();
    }
}
package com.epita.contracts;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.bson.codecs.pojo.annotations.BsonProperty;

import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection="posts")
public class PostsContract {

    @BsonProperty("_id")
    private UUID postId;
    private UUID userId;
    private String content;
    private List<String> hashtags;
    private String mediaUrl;
    private UUID repostOf;
    private UUID replyTo;
    private Instant createdAt;
    
    public PostsContract() {
    }

    public PostsContract(UUID postId, UUID authorId, String content, List<String> hashtags, String mediaUrl, UUID repostOf, UUID replyTo, Instant createdAt) {
        this.postId = postId;
        this.userId = authorId;
        this.content = content;
        this.hashtags = hashtags;
        this.mediaUrl = mediaUrl;
        this.repostOf = repostOf;
        this.replyTo = replyTo;
        this.createdAt = createdAt;
    }

    public UUID getPostId() {
        return postId;
    }

    public void setPostId(UUID postId) {
        this.postId = postId;
    }

    public UUID getAuthorId() {
        return userId;
    }

    public void setAuthorId(UUID authorId) {
        this.userId = authorId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getHashtags() {
        return hashtags;
    }

    public void setHashtags(List<String> hashtags) {
        this.hashtags = hashtags;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public UUID getRepostOf() {
        return repostOf;
    }

    public void setRepostOf(UUID repostOf) {
        this.repostOf = repostOf;
    }

    public UUID getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(UUID replyTo) {
        this.replyTo = replyTo;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}

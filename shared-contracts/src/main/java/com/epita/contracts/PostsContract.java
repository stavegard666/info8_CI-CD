package com.epita.contracts;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class PostsContract {
    private UUID postId;
    private UUID authorId;
    private UUID userId;
    private String content;
    private List<String> hashtags;
    private String mediaUrl;
    private UUID repostOf;
    private UUID replyTo;
    private Date createdAt;
    
    public PostsContract() {
    }

    public PostsContract(UUID postId, UUID authorId, UUID userId, String content, List<String> hashtags, String mediaUrl, UUID repostOf, UUID replyTo, Date createdAt) {
        this.postId = postId;
        this.authorId = authorId;
        this.userId = userId;
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
        return authorId;
    }

    public void setAuthorId(UUID authorId) {
        this.authorId = authorId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}

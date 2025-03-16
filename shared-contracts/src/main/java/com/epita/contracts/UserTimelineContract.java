package com.epita.contracts;

import java.util.List;
import java.util.UUID;

import io.quarkus.mongodb.panache.common.MongoEntity;


@MongoEntity(collection="user-timeline")
public class UserTimelineContract {
    
    public UUID userId;
    public List<PostsContract> posts;

    public UserTimelineContract() {
    }

    public UserTimelineContract(UUID userId, List<PostsContract> posts) {
        this.userId = userId;
        this.posts = posts;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public List<PostsContract> getPosts() {
        return posts;
    }

    public void setPosts(List<PostsContract> posts) {
        this.posts = posts;
    }
}

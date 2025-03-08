package com.epita.contracts;

import java.util.Dictionary;
import java.util.List;
import java.util.UUID;

public class HomeTimelineContract {
    private UUID userId;
    private List<Dictionary<String, Object>> posts;

    public HomeTimelineContract() {
    }

    public HomeTimelineContract(UUID userId, List<Dictionary<String, Object>> posts) {
        this.userId = userId;
        this.posts = posts;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public List<Dictionary<String, Object>> getPosts() {
        return posts;
    }

    public void setPosts(List<Dictionary<String, Object>> posts) {
        this.posts = posts;
    }
}

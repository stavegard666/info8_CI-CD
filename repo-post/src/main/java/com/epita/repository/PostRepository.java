package com.epita.repository;

import com.epita.contracts.PostsContract;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class PostRepository implements PanacheMongoRepository<PostsContract> {

    public List<PostsContract> getUserPosts(UUID userId) {
        return find("userId", userId).list();
    }

    public Optional<PostsContract> getPostById(UUID postId) {
        return find("postId", postId).firstResultOptional();
    }

    public Optional<PostsContract> getReplyPost(UUID postId) {
        return find("replyTo", postId).firstResultOptional();
    }
    
    public void savePost(PostsContract post) {
        persist(post);
    }
}

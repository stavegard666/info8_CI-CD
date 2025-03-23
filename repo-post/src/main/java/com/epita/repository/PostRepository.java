package com.epita.repository;

import com.epita.repository.entity.PostsEntity;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class PostRepository implements PanacheMongoRepository<PostsEntity> {

    public List<PostsEntity> getUserPosts(UUID userId) {
        return find("userId", userId).list();
    }

    public Optional<PostsEntity>                                                                                                                                                                                                                                                          getPostById(UUID postId) {
        return find("postId", postId).firstResultOptional();
    }

    public Optional<PostsEntity> getReplyPost(UUID postId) {
        return find("replyTo", postId).firstResultOptional();
    }
    
    public void savePost(PostsEntity post) {
        persist(post);
    }
}

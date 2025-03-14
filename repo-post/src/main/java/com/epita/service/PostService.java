package com.epita.service;

import com.epita.contracts.PostsContract;
import com.epita.repository.PostRepository;
import com.epita.repository.UserRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class PostService {

    @Inject
    private PostRepository postRepository;

    @Inject
    private UserRepository userRepository;

    public PostsContract createPost(PostsContract contract) {
        // Validate post content
        if (contract == null || contract.getContent() == null || contract.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Post content is required.");
        }
        
        // Subject: """Post can contain text, a media, a repost. At least one of, at most two of"""
        int nbElements = 1;

        if (contract.getMediaUrl() != null) nbElements++;

        // Validate number of elements
        if (nbElements < 1 || nbElements > 2) {
            throw new IllegalArgumentException("Invalid number of elements.");
        }
        
        if (contract.getRepostOf() != null)
        {
            // Validate repost ID
            var repostPost = postRepository.getPostById(contract.getRepostOf());
            if (repostPost.isEmpty()) {
                throw new IllegalArgumentException("Valid repostOf is required.");
            }
            // Check authorization
            if (userRepository.findUserById(repostPost.get().getAuthorId()).isEmpty()) {
                throw new IllegalArgumentException("User not authorized to repost this post.");
            }
            nbElements++;
        }
        
        if (contract.getReplyTo() != null) {
            // Validate reply ID
            var replyPost = postRepository.getPostById(contract.getReplyTo());
            if (replyPost.isEmpty()) {
                throw new IllegalArgumentException("Valid replyTo is required.");
            }
            // Check authorization
            if (userRepository.findUserById(replyPost.get().getAuthorId()).isEmpty()) {
                throw new IllegalArgumentException("User not authorized to reply to this post.");
            }
        }

        // Generate new post ID
        if (contract.getPostId() == null) {
            contract.setPostId(UUID.randomUUID());
        }

        // Set creation timestamp
        if (contract.getCreatedAt() == null) {
            contract.setCreatedAt(Instant.now());
        }

        // Validate author ID
        if (contract.getAuthorId() == null || userRepository.findUserById(contract.getAuthorId()).isEmpty()) {
            throw new IllegalArgumentException("Valid authorId is required.");
        }

        // Save the post
        postRepository.savePost(contract);
        return contract;
    }

    public void deleteOwnPost(UUID userId, UUID postId) {
        // Retrieve post by ID
        PostsContract post = postRepository.getPostById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found."));

        // Check authorization
        if (!post.getAuthorId().equals(userId)) {
            throw new SecurityException("User not authorized to delete this post.");
        }

        // Delete the post
        postRepository.delete(post);
    }

    public List<PostsContract> getUserPosts(UUID userId) {
        // Validate user existence
        if (userRepository.findUserById(userId).isEmpty()) {
            throw new IllegalArgumentException("User not found.");
        }

        // Retrieve user's posts
        return postRepository.getUserPosts(userId);
    }

    public PostsContract getPost(UUID postId) {
        // Retrieve post by ID
        return postRepository.getPostById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found."));
    }

    public PostsContract getReplyPost(UUID postId) {
        // Retrieve reply post by ID
        return postRepository.getReplyPost(postId)
                .orElseThrow(() -> new IllegalArgumentException("Reply post not found."));
    }
}

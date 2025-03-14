package com.epita.service;

import com.epita.contracts.PostsContract;
import com.epita.repository.DeletePublisher;
import com.epita.repository.PostPublisher;
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
    PostRepository postRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    PostPublisher postPublisher;

    @Inject
    DeletePublisher deletePublisher;

    public PostsContract createPost(PostsContract contract) {
        if (contract == null || contract.getContent() == null || contract.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Post content is required.");
        }
        
        int nbElements = 1;

        if (contract.getMediaUrl() != null)
            nbElements++;

        //TODO: Post can contain text, a media, a repost. At least one of, at most two of

        if (contract.getRepostOf() != null)
        {
            var repostPost = postRepository.getPostById(contract.getRepostOf());
            if (repostPost.isEmpty()) {
                throw new IllegalArgumentException("Valid repostOf is required.");
            }
            if (userRepository.findUserById(repostPost.get().getUserId()).isEmpty()) {
                throw new IllegalArgumentException("User not authorized to repost this post.");
            }
        }
        
        if (contract.getReplyTo() != null) {
            var replyPost = postRepository.getPostById(contract.getReplyTo());
            if (replyPost.isEmpty()) {
                throw new IllegalArgumentException("Valid replyTo is required.");
            }
            if (userRepository.findUserById(replyPost.get().getUserId()).isEmpty()) {
                throw new IllegalArgumentException("User not authorized to reply to this post.");
            }
        }

        if (contract.getPostId() == null)
            contract.setPostId(UUID.randomUUID());

        if (contract.getCreatedAt() == null)
            contract.setCreatedAt(Instant.now());

        if (contract.getUserId() == null || userRepository.findUserById(contract.getUserId()).isEmpty())
            throw new IllegalArgumentException("Valid userId is required.");

        postPublisher.publish(contract);
        return contract;
    }

    public void deleteOwnPost(UUID userId, UUID postId) {
        PostsContract post = postRepository.getPostById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found."));

        if (!post.getUserId().equals(userId)) {
            throw new SecurityException("User not authorized to delete this post.");
        }

        postRepository.delete(post);
        deletePublisher.publish(post);
    }

    public List<PostsContract> getUserPosts(UUID userId) {
        if (userRepository.findUserById(userId).isEmpty()) {
            throw new IllegalArgumentException("User not found.");
        }

        return postRepository.getUserPosts(userId);
    }

    public PostsContract getPost(UUID postId) {
        return postRepository.getPostById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found."));
    }

    public PostsContract getReplyPost(UUID postId) {
        return postRepository.getReplyPost(postId)
                .orElseThrow(() -> new IllegalArgumentException("Reply post not found."));
    }
}

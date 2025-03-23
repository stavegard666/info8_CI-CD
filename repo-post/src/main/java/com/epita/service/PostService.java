package com.epita.service;

import com.epita.contracts.PostsContract;
import com.epita.repository.DeletePublisher;
import com.epita.repository.PostPublisher;
import com.epita.repository.PostRepository;
import com.epita.repository.UserRepository;
import com.epita.repository.entity.PostsEntity;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.Instant;
import java.util.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public Optional<PostsContract> createPost(PostsContract contract, StringBuilder builder) {
        if (contract == null || contract.getContent() == null || contract.getContent().trim().isEmpty()) {
            builder.append("Post content is required");
            return Optional.empty(); // Post content is required.
        }

        //TODO: Post can contain text, a media, a repost. At least one of, at most two of

        if (contract.getRepostOf() != null)
        {
            var repostPost = postRepository.getPostById(contract.getRepostOf());
            if (repostPost.isEmpty()) {
                builder.append("RepostId does not exist, Valid repostOf is required.");
                return Optional.empty(); // Valid repostOf is required.
            }
            if (userRepository.findUserById(repostPost.get().getUserId()).isEmpty()) {
                builder.append("User not authorized to repost this post.");
                return Optional.empty(); // User not authorized to repost this post.
            }
        }
        
        if (contract.getReplyTo() != null) {
            var replyPost = postRepository.getPostById(contract.getReplyTo());
            if (replyPost.isEmpty()) {
                builder.append("ReplyId does not exist, Valid replyTo is required.");
                return Optional.empty(); // Valid replyTo is required.
            }
            if (userRepository.findUserById(replyPost.get().getUserId()).isEmpty()) {
                builder.append("User not authorized to reply to this post.");
                return Optional.empty(); // User not authorized to reply to this post.
            }
        }

        if (contract.getUserId() == null || userRepository.findUserById(contract.getUserId()).isEmpty()) {
            builder.append("Valid userId is required.");
            return Optional.empty(); // Valid userId is required.
        }

        // if (contract.getPostId() == null) {
        //     contract.setPostId(UUID.randomUUID());
        // }

        // if (contract.getCreatedAt() == null) {
        //     contract.setCreatedAt(Instant.now());
        // }

        Pattern pattern = Pattern.compile("#\\w+");
        Matcher matcher = pattern.matcher(contract.getContent());
        List<String> hashtags = new ArrayList<>();
        while (matcher.find()) {
            hashtags.add(matcher.group());
        }
        contract.setHashtags(hashtags);

        PostsEntity postsEntity = new PostsEntity(contract);
        postsEntity.persist();
        postPublisher.publish(postsEntity.toContract());


        return Optional.of(postsEntity.toContract());
    }

    public int deleteOwnPost(UUID userId, UUID postId) {
        Optional<PostsEntity> optionalPost = postRepository.getPostById(postId);
        if (optionalPost.isEmpty()) {
            return 1;
        }

        PostsEntity post = optionalPost.get();
        if (!post.getUserId().equals(userId)) {
            return 2;
        }

        postRepository.delete(post);
        deletePublisher.publish(post.toContract());

        return 0;
    }

    public List<PostsContract> getUserPosts(UUID userId) {
        if (userRepository.findUserById(userId).isEmpty()) {
            return Collections.emptyList(); // User not found.
        }

        return postRepository.getUserPosts(userId).stream().map(PostsEntity::toContract).toList();
    }

    public Optional<PostsContract> getPost(UUID postId) {
        return postRepository.getPostById(postId).map(PostsEntity::toContract);
    }

    public Optional<PostsContract> getReplyPost(UUID postId) {
        return postRepository.getReplyPost(postId).map(PostsEntity::toContract);
    }
}

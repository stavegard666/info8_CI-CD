package com.epita.ElasticSearch.contracts;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.epita.contracts.PostsContract;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor // Ajout d'un constructeur sans arguments pour CDI
@ApplicationScoped // Rend la classe compatible avec CDI
public class PostContractElasticSearch {

    private UUID postId;
    private UUID authorId;
    private String content;
    private List<String> hashtags;
    private String mediaUrl;
    private UUID repostOf;
    private UUID replyTo;
    private Instant createdAt;
    private long likesNumber;

    public PostContractElasticSearch fromPostsContract(PostsContract postsContract) {
        long likesNumber = 0;
        return new PostContractElasticSearch(
            postsContract.getPostId(),
            postsContract.getAuthorId(),
            postsContract.getContent(),
            postsContract.getHashtags(),
            postsContract.getMediaUrl(),
            postsContract.getRepostOf(),
            postsContract.getReplyTo(),
            postsContract.getCreatedAt(),
            likesNumber
        );
    }

    public PostsContract toPostsContracts(PostContractElasticSearch postsContract) {
        return new PostsContract(
            postsContract.postId,
            postsContract.authorId,
            postsContract.content,
            postsContract.hashtags,
            postsContract.mediaUrl,
            postsContract.repostOf,
            postsContract.replyTo,
            postsContract.createdAt
        );
    }
}


package com.epita.ElasticSearch.contracts;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostContract {
    private UUID postId;
    private UUID authorId;
    private String content;
    private List<String> hashtags;
    private String mediaUrl;
    private UUID repostOf;
    private UUID replyTo;
    private String createdAt;
}

package com.epita.ElasticSearch.contracts;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.arjuna.ats.internal.arjuna.objectstore.jdbc.drivers.postgres_driver;
import com.epita.ElasticSearch.ElasticSearchRestClient;
import com.epita.contracts.PostsContract;
import com.oracle.svm.core.annotate.Inject;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@ApplicationScoped
public class PostContractElasticSearch {

    public UUID postId;
    public UUID authorId;
    public String content;
    public List<String> hashtags;
    public String mediaUrl;
    public UUID repostOf;
    public UUID replyTo;
    public Instant createdAt;
    public long likesNumber;

    public PostContractElasticSearch fromPostsContract(PostsContract postsContract) {
    long likesNumber = 0;
   /*try {
        likesNumber =  elasticSearchRestClient.search_numbers_of_like_by_postid(postsContract.getPostId());
    } catch (Exception e) {
        likesNumber = 0;
    }*/
    return new PostContractElasticSearch(postsContract.getPostId(), postsContract.getAuthorId(), postsContract.getContent(), postsContract.getHashtags(), postsContract.getMediaUrl(), postsContract.getRepostOf(), postsContract.getReplyTo(), postsContract.getCreatedAt(), likesNumber);
    }

    public PostsContract toPostsContracts(PostContractElasticSearch postsContract) {
        return new PostsContract(postsContract.postId, postsContract.authorId, postsContract.content, postsContract.hashtags, postsContract.mediaUrl, postsContract.repostOf, postsContract.replyTo, postsContract.createdAt);
        }
}


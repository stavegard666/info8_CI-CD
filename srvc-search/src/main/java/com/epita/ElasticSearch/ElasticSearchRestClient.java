package com.epita.ElasticSearch;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.arjuna.ats.internal.jdbc.drivers.modifiers.list;
import com.epita.contracts.PostsContract;
import com.epita.contracts.UsersContract;
import com.epita.ElasticSearch.contracts.PostContractElasticSearch;
import com.epita.contracts.LikesContract;
import com.oracle.svm.core.annotate.Delete;

import co.elastic.clients.elasticsearch.core.CreateRequest;
import co.elastic.clients.elasticsearch.core.DeleteRequest;
import co.elastic.clients.elasticsearch.core.DeleteResponse;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.analysis.Analyzer;
import co.elastic.clients.elasticsearch._types.analysis.AnalyzerVariant;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;

@ApplicationScoped
public class ElasticSearchRestClient {

    @Inject
    ElasticsearchClient elasticsearchClient;

    @Inject
    PostContractElasticSearch postContractElasticSearch;

    public boolean indexExists(String indexName) throws IOException {
        return elasticsearchClient.indices().exists(e -> e.index(indexName)).value();
    }
        
    public void create_posts_with_analyzer() throws IOException {
        // Crée un index avec un analyzer personnalisé
        CreateIndexResponse createIndexResponse = elasticsearchClient.indices().create(c -> c
            .index("posts")
            .settings(s -> s
                .analysis(a -> a
                    .analyzer("posts_analyzer", custom -> custom
                        .custom(ce -> ce.tokenizer("standard").filter("lowercase", "asciifolding", "classic", "decimal_digit", "elision"))
                    )
                )
            )

        );
    }


    public void create_users_with_analyzer() throws IOException {
        // Crée un index avec un analyzer personnalisé
        CreateIndexResponse createIndexResponse = elasticsearchClient.indices().create(c -> c
            .index("users")
            .settings(s -> s
                .analysis(a -> a
                    .analyzer("users_analyzer", custom -> custom
                        .custom(ce -> ce.tokenizer("standard").filter("lowercase", "asciifolding"))
                    )
                )
            )

        );
    }

    public void create_likes() throws IOException {
        // Crée un index avec un analyzer personnalisé
        CreateIndexResponse createIndexResponse = elasticsearchClient.indices().create(c -> c
            .index("likes")
        );
    }

    public void insert_post(PostsContract postContract) throws IOException {
        PostContractElasticSearch postContract2 = postContractElasticSearch.fromPostsContract(postContract);
        IndexRequest<PostContractElasticSearch> request = IndexRequest.of(  
            b -> b.index("posts")
                .id(postContract.getPostId().toString())
                .document(postContract2)); 
        elasticsearchClient.index(request);  
    }

    public void insert_user(UsersContract usersContract) throws IOException {
        IndexRequest<UsersContract> request = IndexRequest.of(  
            b -> b.index("users")
                .id(usersContract.getUserId().toString())
                .document(usersContract)); 
        elasticsearchClient.index(request);  
    }

    public void insert_likes(LikesContract likesContract) throws IOException {
        List<PostContractElasticSearch> list = search_post_by_id(likesContract.getPostId());
        for (PostContractElasticSearch postContractElasticSearch: list ) {
            postContractElasticSearch.setLikesNumber(postContractElasticSearch.getLikesNumber() + 1);
            IndexRequest<PostContractElasticSearch> updateRequest = IndexRequest.of(b -> b
            .index("posts")
            .id(postContractElasticSearch.getPostId().toString())
            .document(postContractElasticSearch)
        );
        elasticsearchClient.index(updateRequest);
        }
        IndexRequest<LikesContract> request = IndexRequest.of(  
            b -> b.index("likes")
                .id(likesContract.getId().toString())
                .document(likesContract)); 
        elasticsearchClient.index(request);  
    }
    public String delete_by_id(String name, UUID userId) throws IOException {
        DeleteRequest searchRequest = DeleteRequest.of( b -> b.index(name).id(userId.toString()));
        String searchResponse = elasticsearchClient.delete(searchRequest).result().toString();
        return "Deleted";
    }
    public List<UsersContract> search_user_by_id(UUID userId) throws IOException {
        SearchRequest searchRequest = SearchRequest.of( b -> b.index("users").query(QueryBuilders.matchPhrase().field("userId").query(userId.toString()).build()._toQuery()));
        List<UsersContract> searchResponse = elasticsearchClient.search(searchRequest, UsersContract.class).hits().hits().stream().map(hit -> hit.source()).collect(java.util.stream.Collectors.toList());
        return searchResponse;
    }
    public List<UsersContract> search_user_by_name(String name) throws IOException {
        SearchRequest searchRequest = SearchRequest.of( b -> b.index("users").query(QueryBuilders.match().field("userName").query(name).analyzer("users_analyzer").fuzziness("AUTO").build()._toQuery()));
        List<UsersContract> searchResponse = elasticsearchClient.search(searchRequest, UsersContract.class).hits().hits().stream().map(hit -> hit.source()).collect(java.util.stream.Collectors.toList());
        return searchResponse;
    }

    public List<PostContractElasticSearch> search_post_by_hashtags(List<String> hashtags) throws IOException {
        SearchRequest searchRequest = SearchRequest.of( b -> b.index("posts").query(QueryBuilders.match().field("hashtags").query(hashtags.toString()).build()._toQuery()));
        List<PostContractElasticSearch> searchResponse = elasticsearchClient.search(searchRequest, PostContractElasticSearch.class).hits().hits().stream().map(hit -> hit.source()).collect(java.util.stream.Collectors.toList());
        return searchResponse;
    }
    public List<PostContractElasticSearch> search_post_by_user(UUID userId) throws IOException {
        SearchRequest searchRequest = SearchRequest.of( b -> b.index("posts").query(QueryBuilders.matchPhrase().field("authorId").query(userId.toString()).build()._toQuery()));
        List<PostContractElasticSearch> searchResponse = elasticsearchClient.search(searchRequest, PostContractElasticSearch.class).hits().hits().stream().map(hit -> hit.source()).collect(java.util.stream.Collectors.toList());
        return searchResponse;
    }
    public List<PostContractElasticSearch> search_post_by_id(UUID id) throws IOException {
        SearchRequest searchRequest = SearchRequest.of( b -> b.index("posts").query(QueryBuilders.matchPhrase().field("postId").query(id.toString()).build()._toQuery()));
        List<PostContractElasticSearch> searchResponse = elasticsearchClient.search(searchRequest, PostContractElasticSearch.class).hits().hits().stream().map(hit -> hit.source()).collect(java.util.stream.Collectors.toList());
        return searchResponse;
    }

    public long search_numbers_of_like_by_postid(UUID postId) throws IOException {
        return elasticsearchClient.count(c -> c.index("likes").query(QueryBuilders.match().field("postId").query(postId.toString()).build()._toQuery())).count();
    }

    public List<PostContractElasticSearch> search_all(String content, List<String> hashtags, List<UUID> userIDs) throws IOException {
        List<Query> must_queries = new ArrayList<>();
        List<Query> should_queries = new ArrayList<>();
        for (String hashtag: hashtags) {
            should_queries.add(QueryBuilders.match().field("hashtags").query(hashtag).analyzer("posts_analyzer").fuzziness("AUTO").build()._toQuery());
        }
        for (UUID userID: userIDs) {
            should_queries.add(QueryBuilders.match().field("authorId").query(userID.toString()).build()._toQuery());
        }
        should_queries.add(QueryBuilders.match().field("content").query(content).analyzer("posts_analyzer").fuzziness("AUTO").build()._toQuery());
        SearchRequest searchRequest = SearchRequest.of( b -> b.index("posts").query(QueryBuilders.bool().should(should_queries).must(must_queries).build()._toQuery()).sort(s -> s.field(f -> f
        .field("likesNumber")
        .order(co.elastic.clients.elasticsearch._types.SortOrder.Desc)
        )));
        List<PostContractElasticSearch> searchResponse = elasticsearchClient.search(searchRequest, PostContractElasticSearch.class).hits().hits().stream().map(hit -> hit.source()).collect(java.util.stream.Collectors.toList());
        return searchResponse;
    }

}

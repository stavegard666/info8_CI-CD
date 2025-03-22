package com.epita.ElasticSearch;


import java.io.IOException;
import java.util.List;
import java.util.UUID;

import com.arjuna.ats.internal.jdbc.drivers.modifiers.list;
import com.epita.ElasticSearch.contracts.PostContract;
import com.epita.ElasticSearch.contracts.UsersContract;
import com.oracle.svm.core.annotate.Delete;

import co.elastic.clients.elasticsearch.core.DeleteRequest;
import co.elastic.clients.elasticsearch.core.DeleteResponse;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
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
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
@ApplicationScoped
public class ElasticSearchRestClient {


    @Inject
    ElasticsearchClient elasticsearchClient;

    public void insert_post(PostContract postContract) throws IOException {
        IndexRequest<PostContract> request = IndexRequest.of(  
            b -> b.index("posts")
                .id(postContract.getPostId().toString())
                .document(postContract)); 
        elasticsearchClient.index(request);  
    }

    public void insert_user(UsersContract usersContract) throws IOException {
        IndexRequest<UsersContract> request = IndexRequest.of(  
            b -> b.index("users")
                .id(usersContract.getUserId().toString())
                .document(usersContract)); 
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
    public List<UsersContract> search_post(UUID userId) throws IOException {
        SearchRequest searchRequest = SearchRequest.of( b -> b.index("posts").query(QueryBuilders.matchPhrase().field("userId").query(userId.toString()).build()._toQuery()));
        List<UsersContract> searchResponse = elasticsearchClient.search(searchRequest, UsersContract.class).hits().hits().stream().map(hit -> hit.source()).collect(java.util.stream.Collectors.toList());
        return searchResponse;
    }

}

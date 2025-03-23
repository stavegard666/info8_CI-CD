package com.epita;

import com.epita.ElasticSearch.ElasticSearchRestClient;
import com.epita.ElasticSearch.contracts.PostContract;
import com.epita.ElasticSearch.contracts.UsersContract;
import com.epita.ElasticSearch.SearchService;
import jakarta.ws.rs.core.Response;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.annotations.Pos;

@Path("/api")
public class srvcSearch {

    @Inject
    ElasticSearchRestClient elasticSearchReastClient;    

    @GET
    @Path("/")
    @Produces(MediaType.TEXT_PLAIN)
    public Response get() {
        return Response.ok().build();
    }

    @POST
    @Path("/add_post")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add_post(List<PostContract> postContracts) {
        String response = "";
        try {
            for(PostContract postContract : postContracts) {
                List<UsersContract> users = elasticSearchReastClient.search_user_by_id(postContract.getAuthorId());
                if(users.size() == 0) {
                    throw new Exception("User not found");
                }
                elasticSearchReastClient.insert_post(postContract);
                response += String.format("Post %s inserted,\n", postContract.getPostId());
            }
        } catch (Exception e) {
            return Response.status(400).entity(e.getMessage() + "\nCan't insert post").build();
        }
        return Response.ok(response + "Finished").build();
    }

    @POST
    @Path("/add_user")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add_user(List<UsersContract> usersContracts) {
        String response = "";
        try {
            for(UsersContract usersContract : usersContracts) {
                elasticSearchReastClient.insert_user(usersContract);
                response += String.format("User %s(%s) inserted,\n", usersContract.getUserName(),usersContract.getUserId());

            }
        } catch (Exception e) {
            return Response.status(400).entity(e.getMessage() + "\nCan't insert user").build();
        }
        return Response.ok(response + "Finished").build();
    }

    @GET
    @Path("/delete/{name}/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete_id(@PathParam("name") String name, @PathParam("id") UUID _id) {
        try {
            return Response.ok(elasticSearchReastClient.delete_by_id(name, _id)).build();
        } catch( IOException r) {
            return Response.status(400).entity(r.getMessage() + "Can't delete").build();
        }
    }

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response search(String search_message) {
        List<PostContract> posts = new ArrayList<>();
        try {
            /* userName search 
            for (String word: search_message.split(" ")) {
                List<UsersContract> users = elasticSearchReastClient.search_user_by_name(word);
                for (UsersContract user: users) {
                    List<PostContract> temp = elasticSearchReastClient.search_post_by_user(user.getUserId());
                    posts.addAll(temp);

                }
            }*/
            
            /* hashtags search
            List<String> hashtags = SearchService.extract_hashtags(search_message);
            posts = elasticSearchReastClient.search_post_by_hashtags(hashtags);*/
        } catch (Exception e) {
            return Response.status(400).entity(e.getMessage() + "Can't search").build();
        }
        return Response.ok(posts).build();
    }
}

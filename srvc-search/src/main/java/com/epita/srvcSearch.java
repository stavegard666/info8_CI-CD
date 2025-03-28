package com.epita;

import com.epita.ElasticSearch.ElasticSearchRestClient;
import com.epita.contracts.BlocksUserContract;
import com.epita.contracts.LikesContract;
import com.epita.contracts.PostsContract;
import com.epita.contracts.UsersContract;
import com.epita.ElasticSearch.SearchService;
import com.epita.ElasticSearch.SearchingStruct;
import com.epita.ElasticSearch.contracts.PostContractElasticSearch;

import jakarta.ws.rs.core.Response;
import jakarta.enterprise.context.ApplicationScoped;
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
@ApplicationScoped
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
    public Response add_post(List<PostsContract> postContracts) {
        String response = "";
        try {
            if (!elasticSearchReastClient.indexExists("posts"))
            {
                elasticSearchReastClient.create_posts_with_analyzer();
            }
            for(PostsContract postContract : postContracts) {
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
            if (!elasticSearchReastClient.indexExists("users"))
            {
                elasticSearchReastClient.create_users_with_analyzer();
            }
            for(UsersContract usersContract : usersContracts) {
                elasticSearchReastClient.insert_user(usersContract);
                response += String.format("User %s(%s) inserted,\n", usersContract.getUserName(),usersContract.getUserId());

            }
        } catch (Exception e) {
            return Response.status(400).entity(e.getMessage() + "\nCan't insert user").build();
        }
        return Response.ok(response + "Finished").build();
    }

    @POST
    @Path("/add_likes")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add_likes(LikesContract likesContract) {
        String response = "";
        try {
            if (!elasticSearchReastClient.indexExists("likes"))
            {
                elasticSearchReastClient.create_likes();
            }
            elasticSearchReastClient.insert_likes(likesContract);
            response += String.format("Like for post %s inserted,\n", likesContract.getPostId());
        } catch (Exception e) {
            return Response.status(400).entity(e.getMessage() + "\nCan't insert like").build();
        }
        return Response.ok(response + "Finished").build();
    }

    @POST
    @Path("/add_blocks")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add_likes(BlocksUserContract blocksUserContract) {
        String response = "";
        try {
            if (!elasticSearchReastClient.indexExists("blocked_users"))
            {
                elasticSearchReastClient.create_blocked_users();
            }
            elasticSearchReastClient.insert_blocked_users(blocksUserContract);
            response += String.format("Blocks for authr %s inserted,\n", blocksUserContract.getBlockerId());
        } catch (Exception e) {
            return Response.status(400).entity(e.getMessage() + "\nCan't insert blocks").build();
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
    public Response search(SearchingStruct searching_struct) {
        String search_message = searching_struct.getSearch_message();
        List<BlocksUserContract> blocks = new ArrayList<>();
        List<PostContractElasticSearch> posts = new ArrayList<>();
        List<String> hashtags = SearchService.extract_hashtags(search_message);
        String content = SearchService.remove_hashtags(search_message);
        List<UUID> users_name = new ArrayList<>();
        try {
            blocks = elasticSearchReastClient.search_blocked_from_blocker_id(searching_struct.getSearcher_id());

            for (String word: search_message.split(" ")) {
                List<UsersContract> users = elasticSearchReastClient.search_user_by_name(word);
                for (UsersContract user: users) {
                    users_name.add(user.getUserId());
                }
            }   
            posts = elasticSearchReastClient.search_all(content, hashtags, users_name, blocks);

        } catch (Exception e) {
            return Response.status(400).entity(e.getMessage() + "Can't search").build();
        }
        return Response.ok(posts).build();
    }
}

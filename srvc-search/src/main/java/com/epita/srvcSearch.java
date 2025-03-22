package com.epita;

import com.epita.ElasticSearch.ElasticSearchRestClient;
import com.epita.ElasticSearch.contracts.PostContract;
import com.epita.ElasticSearch.contracts.UsersContract;

import jakarta.ws.rs.core.Response;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

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


    @GET
    @Path("/get/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response get_index(@PathParam("name") String name) {
        return Response.ok().build();
    }

    @GET
    @Path("/delete/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response delete_index(@PathParam("name") String name) {
        return Response.ok().build();
    }

    @GET
    @Path("/delete/{name}/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response delete_id(@PathParam("name") String name, @PathParam("id") String _id) {
        return Response.ok().build();
    }

    @POST
    @Path("/add_post")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add_post(PostContract postContract) {
        try {
            List<UsersContract> users = elasticSearchReastClient.search_user_by_id(postContract.getAuthorId());
            if(users.size() == 0) {
                throw new Exception("User not found");
            }
            elasticSearchReastClient.insert_post(postContract);
        } catch (Exception e) {
            return Response.status(400).entity(e.getMessage() + "\nCan't insert post").build();
        }
        return Response.ok("Post inserted").build();
    }

    @POST
    @Path("/add_user")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add_user(UsersContract usersContract) {
        try {

            elasticSearchReastClient.insert_user(usersContract);
        } catch (Exception e) {
            return Response.status(400).entity(e.getMessage() + "\nCan't insert post").build();
        }
        return Response.ok("User inserted").build();
    }

    @GET
    @Path("/get_match")
    @Produces(MediaType.TEXT_PLAIN)
    public Response get_with_match() {
        return Response.ok().build();
    }
}

package com.epita.client;

import java.util.UUID;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api")
@RegisterRestClient(configKey = "repo-post")
public interface PostsClient {
    
    @GET
    @Path("/getUserPosts")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserPosts(@HeaderParam("X-userId") UUID userId);


    @GET
    @Path("/getPost")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPost(@HeaderParam("X-postId") UUID postId);
}

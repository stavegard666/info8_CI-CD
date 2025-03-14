package com.epita.controller;

import com.epita.contracts.PostsContract;
import com.epita.service.PostService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

@Path("/api")
public class PostResource {

    @Inject
    private PostService postService;

    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello from Quarkus REST";
    }

    @POST
    @Path("/createPost")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPost(PostsContract contract) {
        try {
            PostsContract createdPost = postService.createPost(contract);
            return Response.status(Response.Status.CREATED).entity(createdPost).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/deletePost/{userId}/{postId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteOwnPost(@PathParam("userId") UUID userId, @PathParam("postId") UUID postId) {
        try {
            postService.deleteOwnPost(userId, postId);
            return Response.ok().entity("Post deleted successfully.").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (SecurityException e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/getUserPosts/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserPosts(@PathParam("userId") UUID userId) {
        try {
            List<PostsContract> posts = postService.getUserPosts(userId);
            return Response.ok(posts).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/getPost/{postId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPost(@PathParam("postId") UUID postId) {
        try {
            PostsContract post = postService.getPost(postId);
            return Response.ok(post).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/getReplyPost/{postId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReplyPost(@PathParam("postId") UUID postId) {
        try {
            PostsContract replyPost = postService.getReplyPost(postId);
            return Response.ok(replyPost).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }
}

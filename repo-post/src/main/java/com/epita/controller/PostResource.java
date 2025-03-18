package com.epita.controller;

import com.epita.contracts.PostsContract;
import com.epita.service.PostService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Path("/api")
public class PostResource {

    @Inject
    PostService postService;

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
        StringBuilder builder = new StringBuilder("Post cannot be created. ");
        Optional<PostsContract> createdPost = postService.createPost(contract, builder);
        if (createdPost.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(builder.toString()).build();
        }
        return Response.status(Response.Status.CREATED).entity(createdPost).build();
    }

    @DELETE
    @Path("/deletePost")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteOwnPost(@HeaderParam("X-userId") UUID userId, @HeaderParam("X-postId") UUID postId) {
        int err = postService.deleteOwnPost(userId, postId);
        return switch (err) {
            case 0 -> Response.ok().entity("Post deleted successfully.").build();
            case 1 -> Response.status(Response.Status.NOT_FOUND).entity("Post not found").build();
            default ->
                    Response.status(Response.Status.FORBIDDEN).entity("User not authorized to delete this post.").build();
        };
    }

    @GET
    @Path("/getUserPosts")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserPosts(@HeaderParam("X-userId") UUID userId) {
        List<PostsContract> posts = postService.getUserPosts(userId);
        if (posts.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found.").build();
        }
        return Response.ok(posts).build();
    }

    @GET
    @Path("/getPost")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPost(@HeaderParam("X-postId") UUID postId) {
        Optional<PostsContract> post = postService.getPost(postId);
        if (post.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("Post not found.").build();
        }

        return Response.ok(post).build();
    }

    @GET
    @Path("/getReplyPost")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReplyPost(@HeaderParam("X-postId") UUID postId) {
        Optional<PostsContract> post = postService.getReplyPost(postId);
        if (post.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("Post not found.").build();
        }

        return Response.ok(post).build();
    }

}

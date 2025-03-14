package com.epita.controller;

import com.epita.contracts.PostsContract;
import com.epita.service.PostService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;
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
        Optional<PostsContract> createdPost = postService.createPost(contract);
        if (createdPost.isPresent()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Post cannot create.").build();
        }
        return Response.status(Response.Status.CREATED).entity(createdPost).build();
    }

    @DELETE
    @Path("/deletePost/{userId}/{postId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteOwnPost(@PathParam("userId") UUID userId, @PathParam("postId") UUID postId) {
        int err = postService.deleteOwnPost(userId, postId);
        return switch (err) {
            case 0 -> Response.ok().entity("Post deleted successfully.").build();
            case 1 -> Response.status(Response.Status.NOT_FOUND).entity("Post not found").build();
            default -> Response.status(Response.Status.FORBIDDEN)
                    .entity("User not authorized to delete this post.").build();
        };
    }

    @GET
    @Path("/getUserPosts/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserPosts(@PathParam("userId") UUID userId) {
        List<PostsContract> posts = postService.getUserPosts(userId);
        if (posts.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found.").build();
        }
        return Response.ok(posts).build();
    }

    @GET
    @Path("/getPost/{postId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPost(@PathParam("postId") UUID postId) {
        Optional<PostsContract> optionalPost = postService.getPost(postId);
        if (optionalPost.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("Post not found.").build();
        }

        PostsContract post = optionalPost.get();
        return Response.ok(post).build();
    }

    @GET
    @Path("/getReplyPost/{postId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReplyPost(@PathParam("postId") UUID postId) {
        Optional<PostsContract> optionalPost = postService.getReplyPost(postId);
        if (optionalPost.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("Post not found.").build();
        }

        PostsContract post = optionalPost.get();
        return Response.ok(post).build();
    }
}

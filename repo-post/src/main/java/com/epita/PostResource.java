package com.epita;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.epita.contracts.PostsContract;
import com.epita.controller.PostRepository;
import com.epita.controller.UserRepository;

import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api")
public class PostResource {

    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello from Quarkus REST";
    }

    @Inject
    private UserRepository userRepository;

    @Inject
    private PostRepository postRepository;

    @GET
    @Path("/test")
    @Produces(MediaType.APPLICATION_JSON)
    public Response test() {
        return Response.ok(userRepository.listAll()).build();
    }

    @POST
    @Path("/createPost")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPost(PostsContract contract) {
        if (contract == null || contract.getContent() == null || contract.getContent().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Post content is required.")
                           .build();
        }
        
        if (contract.getPostId() == null) {
            contract.setPostId(UUID.randomUUID());
        }
        if (contract.getCreatedAt() == null) {
            contract.setCreatedAt(Instant.now());
        }
        
        if (contract.getAuthorId() == null || userRepository.find("authorId", contract.getAuthorId()) == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Valid authorId is required.")
                           .build();
        }
        
        postRepository.persist(contract);
        return Response.status(Response.Status.CREATED)
                       .entity(contract)
                       .build();
    }

    @DELETE
    @Path("/deletePost/{userId}/{postId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePost(@PathParam("userId") UUID userId, @PathParam("postId") UUID postId) {
        PostsContract post = postRepository.find("postId", postId).firstResult();
        if (post == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Post not found.")
                           .build();
        }
        if (!post.getAuthorId().equals(userId)) {
            return Response.status(Response.Status.FORBIDDEN)
                           .entity("User not authorized to delete this post.")
                           .build();
        }
        postRepository.delete(post);
        return Response.ok()
                       .entity("Post deleted successfully.")
                       .build();
    }

    @GET
    @Path("/getUserPosts/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserPosts(@PathParam("userId") UUID userId) {
        if (userRepository.find("userId", userId) == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("User not found.")
                           .build();
        }
        List<PostsContract> posts = postRepository.find("authorId", userId).list();
        return Response.ok(posts).build();
    }

    @GET
    @Path("/getPost/{postId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPost(@PathParam("postId") UUID postId) {
        PostsContract post = postRepository.find("postId", postId).firstResult();
        if (post == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Post not found.")
                           .build();
        }
        return Response.ok(post).build();
    }

    @GET
    @Path("/getReplyPost/{postId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReplyPost(@PathParam("postId") UUID postId) {
        Optional<PostsContract> postOptional = postRepository.find("replyTo", postId).firstResultOptional();
        if (postOptional.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Reply post not found.")
                           .build();
        }
        return Response.ok(postOptional.get()).build();
    }
}

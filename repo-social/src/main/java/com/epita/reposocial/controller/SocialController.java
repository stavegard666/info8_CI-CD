package com.epita.reposocial.controller;

import com.epita.reposocial.entity.BlockEntity;
import com.epita.reposocial.entity.FollowEntity;
import com.epita.reposocial.entity.LikeEntity;
import com.epita.reposocial.service.SocialServiceImpl;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/social")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SocialController {

    @Inject
    SocialServiceImpl socialService;

    // Like endpoints
    @POST
    @Path("/likes/{userId}/{postId}")
    public Response likePost(@PathParam("userId") String userId, 
                           @PathParam("postId") String postId) {
        LikeEntity like = socialService.likePost(userId, postId);
        return Response.status(Response.Status.CREATED).entity(like).build();
    }

    @DELETE
    @Path("/likes/{userId}/{postId}")
    public Response unlikePost(@PathParam("userId") String userId, 
                             @PathParam("postId") String postId) {
        socialService.unlikePost(userId, postId);
        return Response.noContent().build();
    }

    @GET
    @Path("/likes/post/{postId}")
    public List<String> getLikingUsers(@PathParam("postId") String postId) {
        return socialService.getLikingUsers(postId);
    }

    @GET
    @Path("/likes/user/{userId}")
    public List<String> getUserLikedPosts(@PathParam("userId") String userId) {
        return socialService.getUserLikedPosts(userId);
    }

    // Follow endpoints
    @POST
    @Path("/follows/{followerId}/{followedId}")
    public Response followUser(@PathParam("followerId") String followerId, 
                             @PathParam("followedId") String followedId) {
        FollowEntity follow = socialService.followUser(followerId, followedId);
        return Response.status(Response.Status.CREATED).entity(follow).build();
    }

    @DELETE
    @Path("/follows/{followerId}/{followedId}")
    public Response unfollowUser(@PathParam("followerId") String followerId, 
                               @PathParam("followedId") String followedId) {
        socialService.unfollowUser(followerId, followedId);
        return Response.noContent().build();
    }

    @GET
    @Path("/follows/followers/{userId}")
    public List<String> getUserFollowers(@PathParam("userId") String userId) {
        return socialService.getUserFollowers(userId);
    }

    @GET
    @Path("/follows/following/{userId}")
    public List<String> getUserFollowing(@PathParam("userId") String userId) {
        return socialService.getUserFollowing(userId);
    }

    // Block endpoints
    @POST
    @Path("/blocks/{blockerId}/{blockedId}")
    public Response blockUser(@PathParam("blockerId") String blockerId, 
                            @PathParam("blockedId") String blockedId) {
        BlockEntity block = socialService.blockUser(blockerId, blockedId);
        return Response.status(Response.Status.CREATED).entity(block).build();
    }

    @DELETE
    @Path("/blocks/{blockerId}/{blockedId}")
    public Response unblockUser(@PathParam("blockerId") String blockerId, 
                              @PathParam("blockedId") String blockedId) {
        socialService.unblockUser(blockerId, blockedId);
        return Response.noContent().build();
    }

    @GET
    @Path("/blocks/blocked/{userId}")
    public List<String> getUserBlocked(@PathParam("userId") String userId) {
        return socialService.getUserBlocked(userId);
    }

    @GET
    @Path("/blocks/blockers/{userId}")
    public List<String> getUserBlockers(@PathParam("userId") String userId) {
        return socialService.getUserBlockers(userId);
    }
} 
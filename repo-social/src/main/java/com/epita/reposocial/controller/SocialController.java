package com.epita.reposocial.controller;

import com.epita.reposocial.entity.BlockEntity;
import com.epita.reposocial.entity.FollowEntity;
import com.epita.reposocial.entity.LikeEntity;
import com.epita.reposocial.service.SocialServiceImpl;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Path("/social")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SocialController {
    private static final Logger LOG = Logger.getLogger(SocialController.class);

    @Inject
    SocialServiceImpl socialService;

    /**
     * Convert a string to a deterministic UUID
     * This allows tests to use simple strings like "user1" that always map to the
     * same UUID
     */
    private UUID stringToUUID(String str) {
        try {
            // If it's already a valid UUID, use it directly
            return UUID.fromString(str);
        } catch (IllegalArgumentException e) {
            // Otherwise, generate a deterministic UUID from the string
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
    }

    // Like endpoints
    @POST
    @Path("/likes/{userId}/{postId}")
    public Response likePost(@PathParam("userId") String userIdStr,
            @PathParam("postId") String postIdStr) {
        try {
            UUID userId = stringToUUID(userIdStr);
            UUID postId = stringToUUID(postIdStr);

            LikeEntity like = socialService.likePost(userId, postId);
            return Response.status(Response.Status.CREATED).entity(like).build();
        } catch (WebApplicationException e) {
            LOG.error("Error liking post", e);
            return Response.status(e.getResponse().getStatus())
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            LOG.error("Unexpected error liking post", e);
            return Response.serverError()
                    .entity("Unexpected error: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/likes/{userId}/{postId}")
    public Response unlikePost(@PathParam("userId") String userIdStr,
            @PathParam("postId") String postIdStr) {
        try {
            UUID userId = stringToUUID(userIdStr);
            UUID postId = stringToUUID(postIdStr);

            socialService.unlikePost(userId, postId);
            return Response.noContent().build();
        } catch (Exception e) {
            LOG.error("Unexpected error unliking post", e);
            return Response.serverError()
                    .entity("Unexpected error: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/likes/post/{postId}")
    public Response getLikingUsers(@PathParam("postId") String postIdStr) {
        try {
            UUID postId = stringToUUID(postIdStr);
            List<UUID> likers = socialService.getLikingUsers(postId);
            // Convert UUIDs back to strings for easier testing
            List<String> likerStrings = likers.stream().map(UUID::toString).collect(Collectors.toList());
            return Response.ok(likerStrings).build();
        } catch (Exception e) {
            LOG.error("Unexpected error getting liking users", e);
            return Response.serverError()
                    .entity("Unexpected error: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/likes/user/{userId}")
    public Response getUserLikedPosts(@PathParam("userId") String userIdStr) {
        try {
            UUID userId = UUID.fromString(userIdStr);
            List<UUID> likedPosts = socialService.getUserLikedPosts(userId);
            return Response.ok(likedPosts).build();
        } catch (IllegalArgumentException e) {
            LOG.error("Invalid UUID format", e);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid UUID format: " + e.getMessage())
                    .build();
        } catch (Exception e) {
            LOG.error("Unexpected error getting user liked posts", e);
            return Response.serverError()
                    .entity("Unexpected error: " + e.getMessage())
                    .build();
        }
    }

    // Follow endpoints
    @POST
    @Path("/follows/{followerId}/{followedId}")
    public Response followUser(@PathParam("followerId") String followerIdStr,
            @PathParam("followedId") String followedIdStr) {
        try {
            UUID followerId = UUID.fromString(followerIdStr);
            UUID followedId = UUID.fromString(followedIdStr);

            FollowEntity follow = socialService.followUser(followerId, followedId);
            return Response.status(Response.Status.CREATED).entity(follow).build();
        } catch (IllegalArgumentException e) {
            LOG.error("Invalid UUID format", e);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid UUID format: " + e.getMessage())
                    .build();
        } catch (WebApplicationException e) {
            LOG.error("Error following user", e);
            return Response.status(e.getResponse().getStatus())
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            LOG.error("Unexpected error following user", e);
            return Response.serverError()
                    .entity("Unexpected error: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/follows/{followerId}/{followedId}")
    public Response unfollowUser(@PathParam("followerId") String followerIdStr,
            @PathParam("followedId") String followedIdStr) {
        try {
            UUID followerId = UUID.fromString(followerIdStr);
            UUID followedId = UUID.fromString(followedIdStr);

            socialService.unfollowUser(followerId, followedId);
            return Response.noContent().build();
        } catch (IllegalArgumentException e) {
            LOG.error("Invalid UUID format", e);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid UUID format: " + e.getMessage())
                    .build();
        } catch (Exception e) {
            LOG.error("Unexpected error unfollowing user", e);
            return Response.serverError()
                    .entity("Unexpected error: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/follows/followers/{userId}")
    public Response getUserFollowers(@PathParam("userId") String userIdStr) {
        try {
            UUID userId = UUID.fromString(userIdStr);
            List<UUID> followers = socialService.getUserFollowers(userId);
            return Response.ok(followers).build();
        } catch (IllegalArgumentException e) {
            LOG.error("Invalid UUID format", e);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid UUID format: " + e.getMessage())
                    .build();
        } catch (Exception e) {
            LOG.error("Unexpected error getting user followers", e);
            return Response.serverError()
                    .entity("Unexpected error: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/follows/following/{userId}")
    public Response getUserFollowing(@PathParam("userId") String userIdStr) {
        try {
            UUID userId = UUID.fromString(userIdStr);
            List<UUID> following = socialService.getUserFollowing(userId);
            return Response.ok(following).build();
        } catch (IllegalArgumentException e) {
            LOG.error("Invalid UUID format", e);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid UUID format: " + e.getMessage())
                    .build();
        } catch (Exception e) {
            LOG.error("Unexpected error getting user following", e);
            return Response.serverError()
                    .entity("Unexpected error: " + e.getMessage())
                    .build();
        }
    }

    // Block endpoints
    @POST
    @Path("/blocks/{blockerId}/{blockedId}")
    public Response blockUser(@PathParam("blockerId") String blockerIdStr,
            @PathParam("blockedId") String blockedIdStr) {
        try {
            UUID blockerId = UUID.fromString(blockerIdStr);
            UUID blockedId = UUID.fromString(blockedIdStr);

            BlockEntity block = socialService.blockUser(blockerId, blockedId);
            return Response.status(Response.Status.CREATED).entity(block).build();
        } catch (IllegalArgumentException e) {
            LOG.error("Invalid UUID format", e);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid UUID format: " + e.getMessage())
                    .build();
        } catch (Exception e) {
            LOG.error("Unexpected error blocking user", e);
            return Response.serverError()
                    .entity("Unexpected error: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/blocks/{blockerId}/{blockedId}")
    public Response unblockUser(@PathParam("blockerId") String blockerIdStr,
            @PathParam("blockedId") String blockedIdStr) {
        try {
            UUID blockerId = UUID.fromString(blockerIdStr);
            UUID blockedId = UUID.fromString(blockedIdStr);

            socialService.unblockUser(blockerId, blockedId);
            return Response.noContent().build();
        } catch (IllegalArgumentException e) {
            LOG.error("Invalid UUID format", e);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid UUID format: " + e.getMessage())
                    .build();
        } catch (Exception e) {
            LOG.error("Unexpected error unblocking user", e);
            return Response.serverError()
                    .entity("Unexpected error: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/blocks/blocked/{userId}")
    public Response getUserBlocked(@PathParam("userId") String userIdStr) {
        try {
            UUID userId = UUID.fromString(userIdStr);
            List<UUID> blocked = socialService.getUserBlocked(userId);
            return Response.ok(blocked).build();
        } catch (IllegalArgumentException e) {
            LOG.error("Invalid UUID format", e);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid UUID format: " + e.getMessage())
                    .build();
        } catch (Exception e) {
            LOG.error("Unexpected error getting blocked users", e);
            return Response.serverError()
                    .entity("Unexpected error: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/blocks/blockers/{userId}")
    public Response getUserBlockers(@PathParam("userId") String userIdStr) {
        try {
            UUID userId = UUID.fromString(userIdStr);
            List<UUID> blockers = socialService.getUserBlockers(userId);
            return Response.ok(blockers).build();
        } catch (IllegalArgumentException e) {
            LOG.error("Invalid UUID format", e);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid UUID format: " + e.getMessage())
                    .build();
        } catch (Exception e) {
            LOG.error("Unexpected error getting user blockers", e);
            return Response.serverError()
                    .entity("Unexpected error: " + e.getMessage())
                    .build();
        }
    }

    // Social distance endpoints
    @GET
    @Path("/distance/{user1Id}/{user2Id}")
    public Response getSocialDistance(@PathParam("user1Id") String user1IdStr,
            @PathParam("user2Id") String user2IdStr) {
        try {
            UUID user1Id = stringToUUID(user1IdStr);
            UUID user2Id = stringToUUID(user2IdStr);

            int distance = socialService.getSocialDistance(user1Id, user2Id);
            if (distance == -1) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("No path found between users")
                        .build();
            }

            return Response.ok(distance).build();
        } catch (WebApplicationException e) {
            LOG.error("Error getting social distance", e);
            return Response.status(e.getResponse().getStatus())
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            LOG.error("Unexpected error getting social distance", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An unexpected error occurred")
                    .build();
        }
    }

    @GET
    @Path("/users-at-distance/{userId}/{distance}")
    public Response getUsersAtDistance(@PathParam("userId") String userIdStr,
            @PathParam("distance") int distance) {
        try {
            UUID userId = stringToUUID(userIdStr);

            if (distance < 1) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Distance must be greater than 0")
                        .build();
            }

            List<UUID> users = socialService.getUsersAtDistance(userId, distance);
            return Response.ok(users).build();
        } catch (WebApplicationException e) {
            LOG.error("Error getting users at distance", e);
            return Response.status(e.getResponse().getStatus())
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            LOG.error("Unexpected error getting users at distance", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An unexpected error occurred")
                    .build();
        }
    }
}
package com.epita.controller;

import java.util.UUID;

import com.epita.service.UserTimeLineService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
@Path("/api/user-timeline")
public class UserTimeLineController {
    
    @Inject
    UserTimeLineService userTimeLineService;


    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello from Quarkus REST";
    }


    @GET
    @Path("/getTimeline")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserTimeLine(@HeaderParam("X-userId") UUID userId) {
        return userTimeLineService.getUserTimeLine(userId);
    }

    @POST
    @Path("/createTimeline")
    @Produces(MediaType.APPLICATION_JSON)
    public Response postUserTimeLine(@HeaderParam("X-userId") UUID userId) {
        return userTimeLineService.postUserTimeLine(userId);
    }

    @DELETE
    @Path("/deleteTimeline")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUserTimeLine(@HeaderParam("X-userId") UUID userId) {
        return userTimeLineService.deleteUserTimeLine(userId);
    }


}

package com.epita.controller;

import java.util.UUID;

import com.epita.testContract;
import com.epita.contracts.PostsContract;
import com.epita.service.UserTimeLineService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
@Path("/api/timeline")
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
    @Path("/user/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserTimeLine(@PathParam("userId") UUID userId) {
        return userTimeLineService.getUserTimeLine(userId);
    }

    @POST
    @Path("/user/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response postUserTimeLine(@PathParam("userId") UUID userId) {
        return userTimeLineService.postUserTimeLine(userId);
    }

}

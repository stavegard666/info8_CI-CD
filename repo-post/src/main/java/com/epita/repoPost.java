package com.epita;

import jakarta.inject.Inject;

//import com.epita.contracts.PostsContract;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.epita.controller.UserRepository;

@Path("/api")
public class repoPost {
    
    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello from Quarkus REST";
    }


    @Inject
    private UserRepository userRepository;

    @GET
    @Path("/test")
    @Produces(MediaType.APPLICATION_JSON)
    public Response test() {
        return Response.ok(userRepository.listAll()).build();
    }
}

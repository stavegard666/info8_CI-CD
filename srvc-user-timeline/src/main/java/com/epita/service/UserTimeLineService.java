package com.epita.service;

import java.util.ArrayList;
import java.util.UUID;

import com.epita.contracts.UserTimelineContract;
import com.epita.repository.UserRepository;
import com.epita.repository.UserTimeLineRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;


@ApplicationScoped
public class UserTimeLineService {

    @Inject
    UserTimeLineRepository userTimeLineRepository;

    @Inject
    UserRepository userRepository;

    public Response getUserTimeLine(UUID userId) {

        if (userRepository.find("_id", userId) == null) {
            return Response.status(404).build();
        }


        UserTimelineContract userTimelineContract = userTimeLineRepository.find("userId", userId).firstResult();
        if (userTimelineContract == null) {
            userTimelineContract = new UserTimelineContract();
            userTimelineContract.userId = userId;
            userTimelineContract.posts = new ArrayList<>();
            userTimeLineRepository.persist(userTimelineContract);
        }
        
        return Response.ok(userTimelineContract).build();
    }

    public Response postUserTimeLine(UUID userId) {
        if (userRepository.find("_id", userId) == null) {
            return Response.status(404).build();
        }

        UserTimelineContract userTimelineContract = userTimeLineRepository.find("userId", userId).firstResult();

        return Response.ok().build();
    }
}



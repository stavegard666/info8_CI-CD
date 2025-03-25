package com.epita.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.epita.client.PostsClient;
import com.epita.contracts.PostsContract;
import com.epita.mapper.UserTimelineMapper;
import com.epita.repository.UserRepository;
import com.epita.repository.UserTimeLineRepository;
import com.epita.repository.entity.UserTimelineEntity;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;



@ApplicationScoped
public class UserTimeLineService {

    @Inject
    UserTimeLineRepository userTimeLineRepository;

    @Inject
    UserRepository userRepository;

    @RestClient
    PostsClient postsClient;

    @Inject
    UserTimelineMapper userTimelineMapper;

    public Response getUserTimeLine(UUID userId) {

        if (userRepository.find("_id", userId) == null) {
            return Response.status(404).build();
        }


        UserTimelineEntity userTimelineEntity = userTimeLineRepository.find("userId", userId).firstResult();
        if (userTimelineEntity == null) {
            userTimelineEntity = new UserTimelineEntity(userId, new ArrayList<>());
            userTimeLineRepository.persist(userTimelineEntity);
        }
        
        return Response.ok(userTimelineMapper.toContract(userTimelineEntity)).build();
    }

    public Response postUserTimeLine(UUID userId) {
        
        if (userRepository.find("_id", userId) == null) {
            return Response.status(404).build();
        }

        UserTimelineEntity userTimelineEntity = userTimeLineRepository.find("userId", userId).firstResult();

        if (userTimelineEntity == null) {
            userTimelineEntity = new UserTimelineEntity(userId, new ArrayList<>());
        }
        
        Response response = postsClient.getUserPosts(userId);
        List<PostsContract> postsContracts = response.readEntity(new GenericType<List<PostsContract>>() {});
        
        postsContracts.sort((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt()));

        for (PostsContract postContract : postsContracts)
            userTimelineEntity.getTimeline().add(postContract);

        userTimelineEntity.persistOrUpdate();

        return Response.ok().build();
    }
    public void addPostToTimeline(PostsContract postsContract)
    {
        UserTimelineEntity userTimelineEntity = userTimeLineRepository.find("userId", postsContract.getUserId()).firstResult();
        if (userTimelineEntity == null) {
            userTimelineEntity = new UserTimelineEntity(postsContract.getUserId(), new ArrayList<>());
        }
        userTimelineEntity.getTimeline().add(0, postsContract);
        userTimelineEntity.persistOrUpdate();
    }

    public void deletePostToTimeline(PostsContract postsContract) {
        UserTimelineEntity userTimelineEntity = userTimeLineRepository.find("userId", postsContract.getUserId()).firstResult();
        userTimelineEntity.setTimeline(userTimelineEntity.getTimeline().stream().filter(p -> !p.getPostId().equals(postsContract.getPostId())).toList());
    
        userTimelineEntity.persistOrUpdate();
    }

    public Response deleteUserTimeLine(UUID userId) {
        if (userRepository.find("_id", userId) == null) {
            return Response.status(404).build();
        }
        
        UserTimelineEntity userTimelineEntity = userTimeLineRepository.find("userId", userId).firstResult();
        if (userTimelineEntity != null) {
            userTimelineEntity.delete();
        }
        
        return Response.ok().build();
    }

}



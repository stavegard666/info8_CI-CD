package com.epita.repository.entity;

import java.util.List;
import java.util.UUID;

import org.bson.codecs.pojo.annotations.BsonProperty;

import com.epita.contracts.UserTimelineContract;

import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

@MongoEntity(collection="user-timeline")
@Data
@AllArgsConstructor
public class UserTimelineEntity {
    
    @BsonProperty("_id")
    UUID userId;
    List<PostsEntity> posts;
    List<LikesEntity> likes;


    public UserTimelineContract toContract() {
        List<com.epita.contracts.PostsContract> posts = this.posts.stream().map(PostsEntity::toContract).toList();
        List<com.epita.contracts.LikesContract> likes = this.likes.stream().map(LikesEntity::toContract).toList();
        return new UserTimelineContract();
    }
}

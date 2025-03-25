package com.epita.repository.entity;

import java.util.List;
import java.util.UUID;

import org.bson.codecs.pojo.annotations.BsonProperty;

import com.epita.contracts.PostsContract;

import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@MongoEntity(collection="user-timeline")
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class UserTimelineEntity extends PanacheMongoEntityBase {
    
    @BsonProperty("_id")
    UUID userId;
    List<PostsContract> timeline;
}

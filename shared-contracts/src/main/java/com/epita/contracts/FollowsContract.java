package com.epita.contracts;

import java.time.Instant;
import java.util.UUID;

import org.bson.codecs.pojo.annotations.BsonProperty;

import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class FollowsContract {

    private UUID followerId;
    private UUID followeeId;
    private Instant followedAt;

}

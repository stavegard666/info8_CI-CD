package com.epita.contracts;

import java.time.Instant;
import java.util.UUID;


import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

@MongoEntity(collection="blocks")
@AllArgsConstructor
@Data
public class BlocksUserContract {

    private UUID blockerId;
    private UUID blockedId;
    private Instant blockedAt;
}

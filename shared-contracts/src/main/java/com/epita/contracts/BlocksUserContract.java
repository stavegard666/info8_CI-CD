package com.epita.contracts;

import java.time.Instant;
import java.util.UUID;


import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@MongoEntity(collection="blocks")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class BlocksUserContract {

    private UUID blockerId;
    private UUID blockedId;
    private Instant blockedAt;
}

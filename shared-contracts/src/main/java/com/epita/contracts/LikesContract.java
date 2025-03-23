package com.epita.contracts;

import java.time.Instant;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class LikesContract {

    private UUID userId;
    private UUID postId;
    private Instant likedAt;

}

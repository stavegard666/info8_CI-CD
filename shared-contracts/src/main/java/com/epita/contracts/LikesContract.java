package com.epita.contracts;

import java.time.Instant;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@Data
@NoArgsConstructor
public class LikesContract {

    private UUID userId;
    private UUID postId;
    private Instant likedAt;

}

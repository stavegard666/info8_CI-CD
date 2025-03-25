package com.epita.contracts;

import java.time.Instant;
import java.util.UUID;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BlocksUserContract {

    private UUID blockerId;
    private UUID blockedId;
    private Instant blockedAt;
}

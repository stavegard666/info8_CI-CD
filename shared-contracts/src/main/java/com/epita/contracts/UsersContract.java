package com.epita.contracts;

import java.time.Instant;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class UsersContract {
    
    private UUID userId;
    private String userName;
    private Instant birthDate;
    private String location;

}

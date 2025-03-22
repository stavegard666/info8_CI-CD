package com.epita.ElasticSearch.contracts;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class UsersContract {
    private UUID userId;
    private String userName;
    private String birthDate;
    private String location;

}

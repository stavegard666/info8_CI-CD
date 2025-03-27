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
public class UsersContractElasticSearch {
    public UUID userId;
    public String userName;
    public String birthDate;
    public String location;
}

package com.epita.repository.entity;

import java.time.Instant;
import java.util.UUID;

import org.bson.codecs.pojo.annotations.BsonProperty;

import com.epita.contracts.UsersContract;

import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@MongoEntity(collection = "users")
@AllArgsConstructor
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class UserEntity extends PanacheMongoEntityBase {
    
    @BsonProperty("_id")
    private UUID userId;
    private String userName;
    private Instant birthDate;
    private String location;


    public UsersContract toContract() {
        return new UsersContract(userId, userName, birthDate, location);
    }
}

package com.epita.contracts;

import java.sql.Date;
import java.util.UUID;

public class UsersContract {
    private UUID userId;
    private String userName;
    private Date birthDate;
    private String location;

    public UsersContract() {
    }

    public UsersContract(UUID userId, String userName, Date birthDate, String location) {
        this.userId = userId;
        this.userName = userName;
        this.birthDate = birthDate;
        this.location = location;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

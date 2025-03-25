package com.epita.mapper;

import com.epita.contracts.UserTimelineContract;
import com.epita.repository.entity.UserTimelineEntity;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserTimelineMapper {
    
    public UserTimelineContract toContract(UserTimelineEntity contract) {
        return new UserTimelineContract(contract.getUserId(), contract.getTimeline());
    }
    
}

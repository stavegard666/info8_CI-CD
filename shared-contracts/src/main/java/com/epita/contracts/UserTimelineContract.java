package com.epita.contracts;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class UserTimelineContract {
    private UUID userId;
    private List<Object> timeline;

}

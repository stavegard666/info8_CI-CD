package com.epita.contracts;

import java.util.List;
import java.util.UUID;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@Data
@NoArgsConstructor
public class UserTimelineContract {
    private UUID userId;
    private List<PostConstruct> timeline;

}

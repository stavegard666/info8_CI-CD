package com.epita.contracts;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomeTimelineContract {
    private UUID userId;
    private List<PostsContract> timeline;
}

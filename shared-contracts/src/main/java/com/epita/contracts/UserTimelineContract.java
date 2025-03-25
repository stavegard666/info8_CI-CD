package com.epita.contracts;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserTimelineContract {

    private UUID userId;
    private List<PostsContract> timeline;

}

package com.epita.contracts;

import java.util.Dictionary;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HomeTimelineContract {
    private UUID userId;
    private List<Object> timeline;
}

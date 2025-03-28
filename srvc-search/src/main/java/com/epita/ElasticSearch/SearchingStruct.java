package com.epita.ElasticSearch;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SearchingStruct {
    String search_message;
    UUID searcher_id;
}

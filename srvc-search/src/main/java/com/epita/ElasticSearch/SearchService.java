package com.epita.ElasticSearch;

import java.util.ArrayList;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SearchService {
    public static List<String> extract_hashtags(String inputString) {
        List<String> result = new ArrayList<String>();
            for (String input: inputString.split(" ")) {
                if (input.startsWith("#")) {
                    result.add(input);
                }
            }
        return result;
    }
}

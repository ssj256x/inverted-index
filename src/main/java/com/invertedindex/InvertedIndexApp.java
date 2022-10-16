package com.invertedindex;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.invertedindex.service.InvertedIndexService;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

public class InvertedIndexApp {
    public static void main(String[] args) throws JsonProcessingException {
        InvertedIndexService invertedIndexService = new InvertedIndexService();
        String sourcePath = "C:\\Users\\ssj25\\Desktop\\";
        invertedIndexService.buildIndex(
                Arrays.asList(
                        Objects.requireNonNull(
                                new File(sourcePath).listFiles(f -> f.getName().contains(".txt") || f.getName().contains(".json"))
                        )
                )
        );
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(invertedIndexService.search("business")));
    }
}

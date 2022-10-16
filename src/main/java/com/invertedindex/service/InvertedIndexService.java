package com.invertedindex.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.invertedindex.models.Document;
import com.invertedindex.models.Meta;
import com.invertedindex.models.SearchResult;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvertedIndexService {

    private final Map<String, List<SearchResult>> invertedIndex;
    private final FileService fileService;
    private final ObjectMapper objectMapper;

    public InvertedIndexService() {
        this.invertedIndex = new HashMap<>();
        this.fileService = new FileService();
        this.objectMapper = new ObjectMapper();
    }

    public void buildIndex(List<File> fileList) throws JsonProcessingException {
        for (File file : fileList) {
            Document document = new Document(
                    fileService.getUniqueIdForFile(file),
                    file.getAbsolutePath(),
                    fileService.getFileName(file)
            );

            List<String> wordList = fileService.fetchAllWordsFromFile(file);
            long idx = -1L;
            Map<String, Boolean> newWordDocMap = new HashMap<>();

            for (String word : wordList) {
                idx++;
                String newWordDocKey = document.getId() + "^" + word;
                newWordDocMap.putIfAbsent(newWordDocKey, true);
                boolean newDoc = newWordDocMap.get(newWordDocKey);

                if (!invertedIndex.containsKey(word)) {
                    List<Long> indices = new ArrayList<>();
                    indices.add(idx);
                    SearchResult newSearchResult = new SearchResult(document, new Meta(1L, indices));
                    List<SearchResult> searchResultList = new ArrayList<>();
                    searchResultList.add(newSearchResult);
                    invertedIndex.put(word, searchResultList);

                    if (newDoc) newWordDocMap.put(newWordDocKey, false);
                    continue;
                }

                List<SearchResult> wordDocList = invertedIndex.get(word);

                if (newDoc) {
                    SearchResult newSearchResult = new SearchResult(document, new Meta(0L, new ArrayList<>()));
                    wordDocList.add(newSearchResult);
                    newWordDocMap.put(newWordDocKey, false);
                }

                int lastIdx = wordDocList.size() - 1;
                Meta metaData = wordDocList.get(lastIdx).getMeta();
                metaData.setOccurrences(metaData.getOccurrences() + 1);
                metaData.getIndices().add(idx);
            }
        }
        fileService.writeToFile(
                objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(invertedIndex),
                "C:\\Users\\ssj25\\Desktop\\inverted-index\\output.json"
        );
    }

    public List<SearchResult> search(String word) {
        return invertedIndex.get(word);
    }
}

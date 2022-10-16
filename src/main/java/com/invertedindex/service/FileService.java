package com.invertedindex.service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileService {

    public List<String> fetchAllWordsFromFile(File file) {
        Pattern pattern = Pattern.compile("[a-zA-Z-@]+");
        List<String> listOfWords = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while((line = br.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) {
                    listOfWords.add(matcher.group().toLowerCase());
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return listOfWords;
    }

    public String getFileName(File file) {
        String path = file.getAbsolutePath();
        return path.substring(path.lastIndexOf("\\") + 1);
    }

    public String getUniqueIdForFile(File file) {
        return UUID.randomUUID().toString();
    }

    public void writeToFile(String text, String path) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            bw.write(text);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}

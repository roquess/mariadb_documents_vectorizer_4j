package com.example.service;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import java.io.File;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.ArrayList;


@Slf4j
public class DocumentParser {
    private final Tika tika;

    public DocumentParser() {
        this.tika = new Tika();
    }

    public List<String> parseDocument(File file) {
        try {
            String content = tika.parseToString(file);
            return split(content, 200, 30);
        } catch (IOException | TikaException e) {
            log.error("Error parsing document: " + file.getName(), e);
            throw new RuntimeException("Failed to parse document", e);
        }
    }

    private List<String> split(String content, int chunkSize, int overlap) {
        List<String> chunks = new ArrayList<>();
        String[] words = content.split("\\s+");

        int start = 0;
        while(start < words.length) {
            int end = Math.min(start + chunkSize, words.length);

            StringBuilder chunk = new StringBuilder();
            for(int i = start; i < end; i++) {
                chunk.append(words[i]).append(" ");
            }
            chunks.add(chunk.toString().trim());

            start += chunkSize - overlap;
        }

        return chunks;
    }

}


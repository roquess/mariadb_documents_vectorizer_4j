package com.example.service;

import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Vectorizer {
    private static final int VECTOR_SIZE = 384;

    public List<Float> vectorize(String text) {
        List<Float> vector = new ArrayList<>(VECTOR_SIZE);
        for (int i = 0; i < VECTOR_SIZE; i++) {
            vector.add((float) Math.random());
        }
        return vector;
    }
}

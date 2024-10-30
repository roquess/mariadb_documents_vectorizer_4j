package com.example.model;

import lombok.Data;
import java.util.List;

@Data
public class Document {
    private String id;
    private String fileName;
    private String content;
    private List<Float> vector;
}

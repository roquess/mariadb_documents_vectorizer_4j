package com.example;

import com.example.model.Document;
import com.example.service.DocumentParser;
import com.example.service.Vectorizer;
import com.example.repository.MariaDBRepository;
import java.io.File;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Slf4j
public class App {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java -jar app.jar <file-path>");
            System.exit(1);
        }

        String dbUrl = "jdbc:mariadb://localhost:3306/vectordb";
        String dbUser = "root";
        String dbPassword = "testvector"; 

        DocumentParser parser = new DocumentParser();
        Vectorizer vectorizer = new Vectorizer();
        MariaDBRepository repository = new MariaDBRepository(dbUrl, dbUser, dbPassword);

        File file = new File(args[0]);
        try {
            List<String> chunkList = parser.parseDocument(file);

            for(String content : chunkList) {
                List<Float> vector = vectorizer.vectorize(content);
                
                Document doc = new Document();
                doc.setId(UUID.randomUUID().toString());
                doc.setFileName(file.getName());
                doc.setContent(content);
                doc.setVector(vector);

                repository.saveDocument(doc);

            }
        } catch (Exception e) {
            log.error("Error processing document", e);
            System.exit(1);
        }
    }
}

package com.example.repository;

import com.example.model.Document;
import java.sql.*;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MariaDBRepository {
    private final String url;
    private final String username;
    private final String password;

    public MariaDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        initializeDatabase();
    }

    private void initializeDatabase() {
        try (Connection conn = getConnection()) {
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("""
                        CREATE TABLE IF NOT EXISTS documents (
                            id VARCHAR(36) PRIMARY KEY,
                            file_name VARCHAR(255),
                            content TEXT,
                            embedding BLOB NOT NULL,
                            VECTOR INDEX (embedding)
                            )
                        """);
            }
        } catch (SQLException e) {
            log.error("Error initializing database", e);
            throw new RuntimeException(e);
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public void saveDocument(Document document) {
        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO documents (id, file_name, content, embedding) VALUES (?, ?, ?, VEC_FromText(?))";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, document.getId());
                pstmt.setString(2, document.getFileName());
                pstmt.setString(3, document.getContent());

                StringBuilder vectorSB = new StringBuilder();
                vectorSB.append("[");
                for(int i = 0; i < document.getVector().size(); i++) {
                    vectorSB.append(document.getVector().get(i));
                    if(i < document.getVector().size() -1) {
                        vectorSB.append(", ");
                    }
                }
                vectorSB.append("]");
                pstmt.setString(4, vectorSB.toString());

                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            log.error("Error saving document", e);
            throw new RuntimeException(e);
        }
    }
}


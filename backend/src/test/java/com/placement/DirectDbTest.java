package com.placement;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.DriverManager;

public class DirectDbTest {
    @Test
    void testDirectConnection() {
        try {
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/college_placement?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Kolkata",
                "root",
                "root"
            );
            System.out.println(">>> DIRECT JDBC CONNECTION SUCCESSFUL: " + conn.getCatalog());
            conn.close();
        } catch (Exception e) {
            System.out.println(">>> DIRECT JDBC CONNECTION FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

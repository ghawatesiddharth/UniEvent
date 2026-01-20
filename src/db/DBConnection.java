package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/unievent_db";
    private static final String USER = "postgres";
    // UPDATE THIS PASSWORD TO MATCH YOUR PGADMIN PASSWORD
    private static final String PASSWORD = "1234"; 

    public static Connection getConnection() throws SQLException {
        try {
            // Load PostgreSQL Driver
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL Driver not found in library path.");
        }
    }
}
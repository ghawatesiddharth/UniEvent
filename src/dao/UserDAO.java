package dao;

import db.DBConnection;
import model.Admin;
import model.Student;
import model.User;
import exception.DatabaseException;
import exception.AuthenticationException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    // Method to Register a User
    public void registerUser(User user) throws DatabaseException {
        String sql = "INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole());

            stmt.executeUpdate();
            System.out.println("Registration Successful for: " + user.getName());

        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) { 
                throw new DatabaseException("Email already exists!");
            }
            throw new DatabaseException("Error registering user: " + e.getMessage());
        }
    }

    // Method to Login
    public User loginUser(String email, String password) throws AuthenticationException, DatabaseException {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = 0;
                try {
                    id = rs.getInt("user_id");
                } catch (SQLException e) {
                    try {
                        id = rs.getInt("id"); 
                    } catch (SQLException ex) {
                         System.err.println("CRITICAL DB ERROR: Could not find 'user_id' OR 'id' column.");
                         throw new DatabaseException("Column mismatch: Check your database columns.");
                    }
                }
                String name = rs.getString("name");
                String role = rs.getString("role");

                User user;
                if ("ADMIN".equalsIgnoreCase(role)) {
                    user = new Admin(name, email, password);
                } else {
                    user = new Student(name, email, password);
                }
                user.setId(id);
                return user;
            } else {
                throw new AuthenticationException("Invalid Email or Password");
            }

        } catch (SQLException e) {
            throw new DatabaseException("Database Error during Login: " + e.getMessage());
        }
    }
}
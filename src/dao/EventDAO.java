package dao;

import db.DBConnection;
import model.Event;
import model.Hackathon;
import model.Workshop;
import exception.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {

    // --- 1. CREATE ---
    public void createEvent(Event event) throws DatabaseException {
        String sql = "INSERT INTO events (title, details, venue, date, capacity, organizer_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, event.getTitle());
            stmt.setString(2, event.getDetails());
            stmt.setString(3, event.getVenue());
            stmt.setDate(4, event.getDate());
            stmt.setInt(5, event.getCapacity());
            stmt.setInt(6, event.getOrganizerId());

            stmt.executeUpdate();
            System.out.println("✅ " + event.getEventType() + " Created Successfully!");

        } catch (SQLException e) {
            throw new DatabaseException("Error creating event: " + e.getMessage());
        }
    }

    // --- 2. READ ALL ---
    public List<Event> getAllEvents() throws DatabaseException {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM events ORDER BY date ASC";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                events.add(mapRowToEvent(rs));
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error fetching events: " + e.getMessage());
        }
        return events;
    }

    // --- 3. READ FOR STUDENT (View My Registrations) ---
    public List<Event> getEventsForStudent(int studentId) throws DatabaseException {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT e.* FROM events e JOIN registrations r ON e.event_id = r.event_id WHERE r.student_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                events.add(mapRowToEvent(rs));
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error fetching user registrations: " + e.getMessage());
        }
        return events;
    }

    // --- 4. UPDATE EVENT ---
    public void updateEvent(int eventId, String newVenue, Date newDate) throws DatabaseException {
        String sql = "UPDATE events SET venue = ?, date = ? WHERE event_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newVenue);
            stmt.setDate(2, newDate);
            stmt.setInt(3, eventId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Event ID " + eventId + " Updated Successfully!");
            } else {
                System.out.println("⚠️ Event ID " + eventId + " not found.");
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error updating event: " + e.getMessage());
        }
    }

    // --- 5. DELETE EVENT ---
    public void deleteEvent(int eventId) throws DatabaseException {
        String sql = "DELETE FROM events WHERE event_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, eventId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Event ID " + eventId + " Deleted Successfully!");
            } else {
                System.out.println("⚠️ Event ID " + eventId + " not found.");
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error deleting event: " + e.getMessage());
        }
    }

    // --- 6. REGISTER STUDENT ---
    public void registerStudent(int studentId, int eventId) throws DatabaseException {
        String sql = "INSERT INTO registrations (student_id, event_id) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            stmt.setInt(2, eventId);
            stmt.executeUpdate();
            System.out.println("✅ Successfully Registered for Event ID: " + eventId);

        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) { 
                System.out.println("⚠️ You are already registered for this event!");
            } else {
                throw new DatabaseException("Registration failed: " + e.getMessage());
            }
        }
    }

    private Event mapRowToEvent(ResultSet rs) throws SQLException {
        int id = rs.getInt("event_id");
        String title = rs.getString("title");
        String details = rs.getString("details");
        if (details == null) details = "No details"; 
        String venue = rs.getString("venue");
        Date date = rs.getDate("date");
        int capacity = rs.getInt("capacity");
        int orgId = rs.getInt("organizer_id");

        Event event;
        if (title.toLowerCase().contains("hackathon")) {
            event = new Hackathon(title, details, venue, date, capacity, orgId);
        } else {
            event = new Workshop(title, details, venue, date, capacity, orgId);
        }
        event.setId(id);
        return event;
    }
}
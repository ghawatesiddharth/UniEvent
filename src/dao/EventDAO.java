package dao;

import db.DBConnection;
import model.Event;
import model.Workshop; // Used for factory logic
import model.Hackathon;
import exception.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {

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

    public List<Event> getAllEvents() throws DatabaseException {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM events ORDER BY date ASC";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Mapping DB row to Object
                int id = rs.getInt("event_id");
                String title = rs.getString("title");
                String details = rs.getString("details");
                String venue = rs.getString("venue");
                Date date = rs.getDate("date");
                int capacity = rs.getInt("capacity");
                int orgId = rs.getInt("organizer_id");

                // Simple logic: If title contains 'Hackathon', make it a Hackathon object
                Event event;
                if (title.toLowerCase().contains("hackathon")) {
                    event = new Hackathon(title, details, venue, date, capacity, orgId);
                } else {
                    event = new Workshop(title, details, venue, date, capacity, orgId);
                }
                event.setId(id);
                events.add(event);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error fetching events: " + e.getMessage());
        }
        return events;
    }
}
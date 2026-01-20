package model;

import java.sql.Date;

public abstract class Event {
    protected int id;
    protected String title;
    protected String details;
    protected String venue;
    protected Date date;
    protected int capacity;
    protected int organizerId;

    public Event(String title, String details, String venue, Date date, int capacity, int organizerId) {
        this.title = title;
        this.details = details;
        this.venue = venue;
        this.date = date;
        this.capacity = capacity;
        this.organizerId = organizerId;
    }

    // Abstract method for Polymorphism
    public abstract String getEventType();

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public String getDetails() { return details; }
    public String getVenue() { return venue; }
    public Date getDate() { return date; }
    public int getCapacity() { return capacity; }
    public int getOrganizerId() { return organizerId; }
    
    @Override
    public String toString() {
        return String.format("[%s] %s | Venue: %s | Date: %s | Slots: %d", 
            getEventType(), title, venue, date, capacity);
    }
}
package model;

import java.sql.Date;

public class Hackathon extends Event {
    public Hackathon(String title, String details, String venue, Date date, int capacity, int organizerId) {
        super(title, details, venue, date, capacity, organizerId);
    }

    @Override
    public String getEventType() {
        return "HACKATHON";
    }
}
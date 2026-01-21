package model;

import java.sql.Date;

public class Workshop extends Event {
    public Workshop(String title, String details, String venue, Date date, int capacity, int organizerId) {
        super(title, details, venue, date, capacity, organizerId);
    }

    public String getEventType() {
        return "WORKSHOP";
    }
}
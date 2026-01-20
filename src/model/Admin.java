package model;

public class Admin extends User {

    public Admin(String name, String email, String password) {
        super(name, email, password, "ADMIN");
    }

    @Override
    public void showDashboard() {
        System.out.println("\n--- ADMIN DASHBOARD ---");
        System.out.println("1. Create New Event");
        System.out.println("2. View All Users");
        System.out.println("3. View Event Registrations");
        System.out.println("4. Logout");
    }
}
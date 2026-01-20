package model;

public class Student extends User {
    
    // Constructor matches Parent
    public Student(String name, String email, String password) {
        super(name, email, password, "STUDENT");
    }

    @Override
    public void showDashboard() {
        System.out.println("\n--- STUDENT DASHBOARD ---");
        System.out.println("1. View Available Events");
        System.out.println("2. Register for Event");
        System.out.println("3. View My Registrations");
        System.out.println("4. Logout");
    }
}
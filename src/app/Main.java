package app;

import dao.UserDAO;
import dao.EventDAO;
import model.*;
import exception.AuthenticationException;
import exception.DatabaseException;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final UserDAO userDAO = new UserDAO();
    private static final EventDAO eventDAO = new EventDAO();
    private static User currentUser = null; // Session variable

    public static void main(String[] args) {
        System.out.println("=================================");
        System.out.println("   WELCOME TO UNIEVENT SYSTEM    ");
        System.out.println("=================================");

        while (true) {
            if (currentUser == null) {
                showGuestMenu();
            } else {
                if (currentUser instanceof Admin) {
                    showAdminMenu();
                } else if (currentUser instanceof Student) {
                    showStudentMenu();
                }
            }
        }
    }

    // --- GUEST MENU ---
    private static void showGuestMenu() {
        System.out.println("\n1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        System.out.print("Enter choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        try {
            switch (choice) {
                case 1 -> login();
                case 2 -> register();
                case 3 -> {
                    System.out.println("Goodbye!");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice.");
            }
        } catch (Exception e) {
            System.out.println("⚠️ Error: " + e.getMessage());
        }
    }

    // --- ADMIN MENU ---
    private static void showAdminMenu() {
        System.out.println("\n--- ADMIN DASHBOARD (" + currentUser.getName() + ") ---");
        System.out.println("1. Create New Event");
        System.out.println("2. View All Events");
        System.out.println("3. Logout");
        System.out.print("Enter choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        try {
            switch (choice) {
                case 1 -> createEvent();
                case 2 -> listEvents();
                case 3 -> logout();
                default -> System.out.println("Invalid choice.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // --- STUDENT MENU ---
    private static void showStudentMenu() {
        System.out.println("\n--- STUDENT DASHBOARD (" + currentUser.getName() + ") ---");
        System.out.println("1. View Upcoming Events");
        System.out.println("2. Logout");
        System.out.print("Enter choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        try {
            switch (choice) {
                case 1 -> listEvents();
                case 2 -> logout();
                default -> System.out.println("Invalid choice.");
            }
        } catch (Exception e) {
            System.out.println("⚠️ Error: " + e.getMessage());
        }
    }

    // --- LOGIC METHODS ---

    private static void register() throws DatabaseException {
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        
        System.out.println("Select Role: 1. Student  2. Admin");
        int roleChoice = scanner.nextInt();
        scanner.nextLine();

        User newUser;
        if (roleChoice == 2) {
            newUser = new Admin(name, email, password);
        } else {
            newUser = new Student(name, email, password);
        }

        userDAO.registerUser(newUser);
    }

    private static void login() throws AuthenticationException, DatabaseException {
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        currentUser = userDAO.loginUser(email, password);
        System.out.println("✅ Login Successful! Welcome " + currentUser.getName());
    }

    private static void logout() {
        currentUser = null;
        System.out.println("Logged out successfully.");
    }

    private static void createEvent() throws DatabaseException {
        System.out.println("\n--- Create New Event ---");
        System.out.print("Enter Title: ");
        String title = scanner.nextLine();
        System.out.print("Enter Details: ");
        String details = scanner.nextLine();
        System.out.print("Enter Venue: ");
        String venue = scanner.nextLine();
        System.out.print("Enter Date (YYYY-MM-DD): ");
        String dateStr = scanner.nextLine();
        System.out.print("Enter Capacity: ");
        int capacity = scanner.nextInt();
        scanner.nextLine();

        // Admin decides type (Polymorphism in action)
        System.out.print("Type: 1. Workshop  2. Hackathon: ");
        int type = scanner.nextInt();
        scanner.nextLine();

        Event event;
        if (type == 2) {
            event = new Hackathon(title, details, venue, Date.valueOf(dateStr), capacity, currentUser.getId());
        } else {
            event = new Workshop(title, details, venue, Date.valueOf(dateStr), capacity, currentUser.getId());
        }

        eventDAO.createEvent(event);
    }

    private static void listEvents() throws DatabaseException {
        List<Event> events = eventDAO.getAllEvents();
        System.out.println("\n--- UPCOMING EVENTS ---");
        if (events.isEmpty()) {
            System.out.println("No events found.");
        } else {
            for (Event e : events) {
                System.out.println(e.toString()); // Uses the overridden toString() method
            }
        }
    }
}
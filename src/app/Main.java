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
    private static User currentUser = null; 

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

    private static void showGuestMenu() {
        System.out.println("\n--- GUEST MENU ---");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        System.out.print("Enter choice: ");
        
        if(scanner.hasNextInt()){
            int choice = scanner.nextInt();
            scanner.nextLine();
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
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            scanner.nextLine();
            System.out.println("Please enter a number.");
        }
    }
    private static void showAdminMenu() {
        System.out.println("\n--- ADMIN DASHBOARD (" + currentUser.getName() + ") ---");
        System.out.println("1. Create New Event");
        System.out.println("2. View All Events");
        System.out.println("3. Update Event Details"); 
        System.out.println("4. Delete Event");       
        System.out.println("5. Logout");
        System.out.print("Enter choice: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();

        try {
            switch (choice) {
                case 1 -> createEvent();
                case 2 -> listEvents();
                case 3 -> updateEvent(); 
                case 4 -> deleteEvent(); 
                case 5 -> logout();
                default -> System.out.println("Invalid choice.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void showStudentMenu() {
        System.out.println("\n--- STUDENT DASHBOARD (" + currentUser.getName() + ") ---");
        System.out.println("1. View Upcoming Events");
        System.out.println("2. Register for Event");    
        System.out.println("3. View My Registrations");
        System.out.println("4. Logout");
        System.out.print("Enter choice: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();

        try {
            switch (choice) {
                case 1 -> listEvents();
                case 2 -> registerForEvent();
                case 3 -> listMyEvents();    
                case 4 -> logout();
                default -> System.out.println("Invalid choice.");
            }
        } catch (Exception e) {
            System.out.println(" Error: " + e.getMessage());
        }
    }

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
        System.out.println("Login Successful! Welcome " + currentUser.getName());
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
    private static void updateEvent() throws DatabaseException {
        listEvents(); 
        System.out.println("\n--- Update Event ---");
        System.out.print("Enter Event ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter New Venue: ");
        String venue = scanner.nextLine();
        System.out.print("Enter New Date (YYYY-MM-DD): ");
        String dateStr = scanner.nextLine();

        eventDAO.updateEvent(id, venue, Date.valueOf(dateStr));
    }
    private static void deleteEvent() throws DatabaseException {
        listEvents();
        System.out.println("\n--- Delete Event ---");
        System.out.print("Enter Event ID to delete: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Are you sure you want to delete Event ID " + id + "? (yes/no): ");
        String confirm = scanner.nextLine();
        
        if (confirm.equalsIgnoreCase("yes")) {
            eventDAO.deleteEvent(id);
        } else {
            System.out.println("Delete cancelled.");
        }
    }
    private static void registerForEvent() throws DatabaseException {
        System.out.print("Enter Event ID to Register: ");
        int eventId = scanner.nextInt();
        scanner.nextLine();

        eventDAO.registerStudent(currentUser.getId(), eventId);
    }

    private static void listEvents() throws DatabaseException {
        List<Event> events = eventDAO.getAllEvents();
        System.out.println("\n--- ALL UPCOMING EVENTS ---");
        printEventList(events);
    }
    private static void listMyEvents() throws DatabaseException {
        List<Event> events = eventDAO.getEventsForStudent(currentUser.getId());
        System.out.println("\n--- MY REGISTRATIONS ---");
        printEventList(events);
    }
        private static void printEventList(List<Event> events) {
        if (events.isEmpty()) {
            System.out.println("No events found.");
        } else {
            for (Event e : events) {
                System.out.println("ID: " + e.getId() + " | " + e.getTitle() + " (" + e.getEventType() + ")");
                System.out.println("" + e.getVenue() + " | " + e.getDate());
                System.out.println("" + e.getDetails());
                System.out.println("-----------------------------------");
            }
        }
    }
}
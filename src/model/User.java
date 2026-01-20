package model;

// Abstract class: You cannot create a generic 'User', it must be a Student or Admin
public abstract class User {
    protected int id;
    protected String name;
    protected String email;
    protected String password;
    protected String role; // "STUDENT" or "ADMIN"

    // Constructor
    public User(String name, String email, String password, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRole() { return role; }

    // Abstract method: Every child must implement their own dashboard view
    public abstract void showDashboard();
    
    @Override
    public String toString() {
        return "User [ID=" + id + ", Name=" + name + ", Role=" + role + "]";
    }
}
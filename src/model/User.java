package model;

public abstract class User {
    protected int id;
    protected String name;
    protected String email;
    protected String password;
    protected String role; 
    public User(String name, String email, String password, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRole() { return role; }

    public abstract void showDashboard();
    
    public String toString() {
        return "User [ID=" + id + ", Name=" + name + ", Role=" + role + "]";
    }
}
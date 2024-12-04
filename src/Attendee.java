public class Attendee {
    private String id;
    private String name;
    private String email;

    // Constructor
    public Attendee(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Business Logic
    public void updateProfile(String newName, String newEmail) {
        this.name = newName;
        this.email = newEmail;
    }
}

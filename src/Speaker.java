public class Speaker {
    private String id;
    private String name;
    private String bio;

    // Constructor
    public Speaker(String id, String name, String bio) {
        this.id = id;
        this.name = name;
        this.bio = bio;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBio() {
        return bio;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    // Business Logic
    public void updateProfile(String newName, String newBio) {
        this.name = newName;
        this.bio = newBio;
    }
}

import java.util.HashSet;
import java.util.Set;

public class Speaker {
    private String id;
    private String name;
    private String bio;

    public Speaker(String id, String name, String bio) {
        this.id = id;
        this.name = name;
        this.bio = bio;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBio() {
        return bio;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    // Method to get all sessions this speaker is assigned to
    public Set<Session> getSessions(SessionRepository sessionRepository) {
        Set<Session> speakerSessions = new HashSet<>();

        // Iterate through all sessions and add those where the speaker is assigned
        for (Session session : sessionRepository.findAll()) {
            if (session.getSpeakers().contains(this)) { // Assuming session has a 'getSpeakers()' method
                speakerSessions.add(session);
            }
        }

        return speakerSessions;
    }

    @Override
    public String toString() {
        return "Speaker{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", bio='" + bio + '\'' +
                '}';
    }
}

import java.util.*;

public class Attendee extends People {
    private List<Conference> registeredConferences; // Tracks conferences the attendee has registered for
    private String password;


    public Attendee(String id, String name, String email, String password) {
        super(id, name, email);
        this.password = password;
        this.registeredConferences = new ArrayList<>();
    }

    // Password getter and setter
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Register for a conference
    public void registerForConference(Conference conference) {
        if (!registeredConferences.contains(conference)) {
            registeredConferences.add(conference);
            conference.addAttendee(this);
        } else {
            throw new IllegalArgumentException("Already registered for this conference.");
        }
    }

    // Get registered conferences
    public List<Conference> getRegisteredConferences() {
        return registeredConferences;
    }

}

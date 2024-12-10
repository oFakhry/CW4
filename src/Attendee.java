import java.util.*;

public class Attendee extends People {
    private List<Conference> registeredConferences; // Tracks conferences the attendee has registered for

    public Attendee(String id, String name, String email) {
        super(id, name, email);
        this.registeredConferences = new ArrayList<>();
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

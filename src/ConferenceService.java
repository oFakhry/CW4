import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ConferenceService {
    private List<Conference> conferences = new ArrayList<>();
    private final SessionService sessionService; // SessionService dependency

    // Constructor to initialize the ConferenceService with SessionService
    public ConferenceService(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    // Create a new conference and associate it with a SessionService
    public void createConference(String id, String name, String description, LocalDate startDate, LocalDate endDate) {
        // Create the conference with the session service
        conferences.add(new Conference(id, name, description, startDate, endDate, sessionService));
    }

    // Register an attendee to a specific conference
    public void registerAttendee(Attendee attendee, String conferenceName) {
        for (Conference conference : conferences) {
            if (conference.getName().equalsIgnoreCase(conferenceName)) {
                conference.addAttendee(attendee);
                return;
            }
        }
        System.out.println("Conference not found.");
    }

    // Register a session to a specific conference
    public void registerSession(Session session, String conferenceName) {
        for (Conference conference : conferences) {
            if (conference.getName().equalsIgnoreCase(conferenceName)) {
                conference.addSession(session);
                return;
            }
        }
        System.out.println("Conference not found.");
    }

    // Get all sessions for a specific conference
    public List<Session> getConferenceSessions(String conferenceName) {
        for (Conference conference : conferences) {
            if (conference.getName().equalsIgnoreCase(conferenceName)) {
                return conference.getSessions();
            }
        }
        return new ArrayList<>();
    }
}

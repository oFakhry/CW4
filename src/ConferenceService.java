import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ConferenceService {
    private List<Conference> conferences = new ArrayList<>();

    public void createConference(String id, String name, String description, LocalDate startDate, LocalDate endDate) {
        conferences.add(new Conference( id, name, description, startDate, endDate));
    }

    public void registerAttendee(Attendee attendee, String conferenceName) {
        for (Conference conference : conferences) {
            if (conference.getName().equalsIgnoreCase(conferenceName)) {
                conference.addAttendee(attendee);
                return;
            }
        }
        System.out.println("Conference not found.");
    }

    public void registerSession(Session session, String conferenceName) {
        for (Conference conference : conferences) {
            if (conference.getName().equalsIgnoreCase(conferenceName)) {
                conference.addSession(session);
                return;
            }
        }
        System.out.println("Conference not found.");
    }

    public List<Session> getConferenceSessions(String conferenceName) {
        for (Conference conference : conferences) {
            if (conference.getName().equalsIgnoreCase(conferenceName)) {
                return conference.getSessions();
            }
        }
        return new ArrayList<>();
    }
}

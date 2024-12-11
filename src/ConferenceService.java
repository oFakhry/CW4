import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ConferenceService {
    private List<Conference> conferences = new ArrayList<>();
    final SessionService sessionService; // SessionService dependency
    private final ConferenceRepository conferenceRepository; // Repository for persistence

    // Constructor to initialize the ConferenceService with dependencies
    public ConferenceService(SessionService sessionService, ConferenceRepository conferenceRepository) {
        this.sessionService = sessionService;
        this.conferenceRepository = conferenceRepository;
        this.conferences = conferenceRepository.loadConferencesFromFile();
    }

    // Create a new conference and associate it with a SessionService
    public void createConference(String id, String name, String description, LocalDate startDate, LocalDate endDate) {
        Conference conference = new Conference(id, name, description, startDate, endDate, sessionService);
        conferences.add(conference);
        saveConferencesToFile(conferences);
    }

    // Get all conferences
    public List<Conference> getConferences() {
        return new ArrayList<>(conferences);
    }

    // Register an attendee to a specific conference
    public void registerAttendee(Attendee attendee, String conferenceName) {
        for (Conference conference : conferences) {
            if (conference.getName().equalsIgnoreCase(conferenceName)) {
                conference.addAttendee(attendee);
                saveConferencesToFile(conferences);
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
                saveConferencesToFile(conferences);
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

    // Saves the provided list of conferences to the file
    public void saveConferencesToFile(List<Conference> conferencesToSave) {
        conferenceRepository.saveConferencesToFile(conferencesToSave);
        // Update the in-memory list to reflect what was just saved
        this.conferences = new ArrayList<>(conferencesToSave);
}}

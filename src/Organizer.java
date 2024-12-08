import java.time.LocalDate;
import java.util.*;

public class Organizer extends People {
    private Set<Conference> conferences;
    private Set<Speaker> speakers;

    public Organizer(String id, String name, String email) {
        super(id, name, email);
        this.conferences = new HashSet<>();
        this.speakers = new HashSet<>();
    }

    // Set up a conference
    public void setUpConference(Conference conference) {
        conferences.add(conference);
    }

    // Register a speaker
    public void registerSpeaker(Speaker speaker) {
        speakers.add(speaker);
    }

    // Get the registered speakers for this organizer
    public Set<Speaker> getRegisteredSpeakers() {
        return speakers;
    }

    // Assign speaker to a session
    public void assignSpeakerToSession(Speaker speaker, Session session) {
        if (!speakers.contains(speaker)) {
            throw new IllegalArgumentException("Speaker not registered.");
        }
        session.addSpeaker(speaker);
    }

    // Manage attendee feedback
    public void manageAttendeeFeedback(Attendee attendee, Session session, String feedback) {
        session.addFeedback(attendee, feedback);
    }

    // Manage conference details
    public void updateConferenceDetails(String conferenceId, String name, String description, LocalDate startDate, LocalDate endDate) {
        Conference conference = findConferenceById(conferenceId);
        if (conference != null) {
            conference.setName(name);
            conference.setDescription(description);
            conference.setStartDate(startDate);
            conference.setEndDate(endDate);
        }
    }

    // Find a conference by ID
    public Conference findConferenceById(String conferenceId) {
        return conferences.stream()
                .filter(conference -> conference.getId().equals(conferenceId))
                .findFirst()
                .orElse(null);
    }

    // Get conferences for this organizer
    public Set<Conference> getConferences() {
        return conferences;
    }
}

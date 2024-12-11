import java.time.LocalDate;
import java.util.*;

public class Organizer extends People {
    private Set<Conference> conferences;
    private Set<Speaker> speakers;
    private String password; // Add password field


    public Organizer(String id, String name, String email,  String password) {
        super(id, name, email);
        this.conferences = new HashSet<>();
        this.speakers = new HashSet<>();
        this.password = password; // Initialize password

    }

    // Get the password
    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Set a new password
    public void setPassword(String password) {
        this.password = password;
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

    // Manage attendee feedback using attendeeId
    public void manageAttendeeFeedback(String attendeeId, String conferenceId, String feedbackText, FeedbackRepository feedbackRepository, Session session) {
        if (attendeeId == null || attendeeId.isEmpty()) {
            throw new IllegalArgumentException("Attendee ID cannot be null or empty.");
        }
        if (conferenceId == null || conferenceId.isEmpty()) {
            throw new IllegalArgumentException("Conference ID cannot be null or empty.");
        }
        if (feedbackText == null || feedbackText.isEmpty()) {
            throw new IllegalArgumentException("Feedback text cannot be null or empty.");
        }
        if (feedbackRepository == null) {
            throw new IllegalArgumentException("Feedback repository cannot be null.");
        }
        if (session == null) {
            throw new IllegalArgumentException("Session cannot be null.");
        }

        // Add the feedback to the session and repository
        session.addFeedback(attendeeId, conferenceId, feedbackText, feedbackRepository);
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

import java.util.*;

public abstract class People {
    String id;
    String name;
    String email;
    private Map<String, Session> sessionSchedule;

    public People(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.sessionSchedule = new HashMap<>();
    }

    // View conference details (will now return a string instead of printing)
    public String viewConferenceDetails(Conference conference) {
        return "Conference: " + conference.getName() + "\n" +
                "Description: " + conference.getDescription() + "\n" +
                "Start Date: " + conference.getStartDate() + "\n" +
                "End Date: " + conference.getEndDate();
    }

    // Manage schedules (attendees and speakers)
    public void manageSchedule(Session session) {
        sessionSchedule.put(session.getId(), session);
    }

    public String viewSessionsForSpeaker(Speaker speaker, SessionRepository sessionRepository) {
        StringBuilder sb = new StringBuilder();
        sb.append(speaker.getName()).append(" is assigned to the following sessions:\n");

        // Fetch the speaker's sessions using the sessionRepository
        Set<Session> speakerSessions = speaker.getSessions(sessionRepository);

        // If no sessions are assigned, notify the user
        if (speakerSessions.isEmpty()) {
            sb.append("No sessions assigned.\n");
        } else {
            // Otherwise, list all the sessions
            for (Session session : speakerSessions) {
                sb.append(session.getName()).append(" (").append(session.getStartTime()).append(")\n");
            }
        }

        return sb.toString();
    }


    // Create and update a speaker profile
    public void createOrUpdateSpeakerProfile(Speaker speaker, String name, String bio) {
        speaker.setName(name);
        speaker.setBio(bio);
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
}

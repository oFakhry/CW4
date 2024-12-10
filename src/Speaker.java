import java.util.HashSet;
import java.util.Set;

public class Speaker extends People {
    private String bio;
    private Set<String> assignedSessionIds; // IDs of sessions assigned to the speaker

    public Speaker(String id, String name, String email, String bio) {
        super(id, name, email);
        this.bio = bio;
        this.assignedSessionIds = new HashSet<>();
    }

    // Add a session ID to the assigned sessions
    public void assignSession(String sessionId) {
        assignedSessionIds.add(sessionId);
    }

    // Remove a session ID from the assigned sessions
    public void removeSession(String sessionId) {
        assignedSessionIds.remove(sessionId);
    }

    // Get all sessions assigned to the speaker
    public Set<Session> getSessions(SessionRepository sessionRepository) {
        Set<Session> sessions = new HashSet<>();
        for (String sessionId : assignedSessionIds) {
            Session session = sessionRepository.findById(sessionId);
            if (session != null) {
                sessions.add(session);
            }
        }
        return sessions;
    }

    // Retrieve the password from the repository
    public String getPassword(SpeakerRepository speakerRepository) {
        Speaker speaker = speakerRepository.findById(this.getId());
        if (speaker != null) {
            return speakerRepository.getPasswordById(this.getId());
        }
        throw new IllegalArgumentException("Speaker not found in the repository.");
    }

    // Getters and Setters
    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public String getId() {
        return super.getId();
    }

    public void setName(String name) {
        super.name = name;
    }

    public void setId(String id) {
        super.id = id;
    }

    public Set<String> getAssignedSessionIds() {
        return assignedSessionIds;
    }
}

import java.util.HashSet;
import java.util.Set;

public class Speaker extends People {
    private String bio;
    private Set<String> assignedSessionIds; // IDs of sessions assigned to the speaker
    private String password;

    public Speaker(String id, String name, String email, String bio, String password) {
        super(id, name, email);
        this.bio = bio;
        this.password = password;
        this.assignedSessionIds = new HashSet<>();
    }

    // Add a session ID to the assigned sessions using a string ID
    public void assignSession(String sessionId) {
        assignedSessionIds.add(sessionId);
    }

    // Add a session to the speaker by passing a Session object
    public void addSession(Session session) {
        if (session != null) {
            assignedSessionIds.add(session.getId());
        }
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

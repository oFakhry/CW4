import java.time.LocalDate;
import java.util.*;

public class Conference {
    private String id;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Session> sessions = new ArrayList<>();
    private Set<Attendee> attendees;
    private Set<Speaker> speakers;

    // Constructor
    public Conference(String id, String name, String description, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.attendees = new HashSet<>();
        this.speakers = new HashSet<>();
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public List<Session> getSessions() {
        return new ArrayList<>(sessions);
    }

    public Set<Attendee> getAttendees() {
        return attendees;
    }

    public Set<Speaker> getSpeakers() {
        return speakers;
    }


    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    // Business Logic Methods
    public void addSession(Session session) {
        if (sessions.containsKey(session.getId())) {
            throw new IllegalArgumentException("Session with this ID already exists.");
        }
        sessions.put(session.getId(), session);
    }

    public void modifySession(String sessionId, Session updatedSession) {
        if (!sessions.containsKey(sessionId)) {
            throw new IllegalArgumentException("Session with this ID does not exist.");
        }
        sessions.put(sessionId, updatedSession);
    }

    public void removeSession(String sessionId) {
        if (!sessions.containsKey(sessionId)) {
            throw new IllegalArgumentException("Session with this ID does not exist.");
        }
        sessions.remove(sessionId);
    }

    public void registerAttendeeForSession(String sessionId, String attendeeId) {
        if (!sessions.containsKey(sessionId)) {
            throw new IllegalArgumentException("Session with this ID does not exist.");
        }
        Session session = sessions.get(sessionId);
        session.registerAttendee(attendeeId);
    }

    public void deregisterAttendeeFromSession(String sessionId, String attendeeId) {
        if (!sessions.containsKey(sessionId)) {
            throw new IllegalArgumentException("Session with this ID does not exist.");
        }
        Session session = sessions.get(sessionId);
        session.deregisterAttendee(attendeeId);
    }

    public void addAttendee(Attendee attendee) {
        attendees.add(attendee);
    }

    public void removeAttendee(String attendeeId) {
        attendees.removeIf(attendee -> attendee.getId().equals(attendeeId));
    }

    public void addSpeaker(Speaker speaker) {
        speakers.add(speaker);
    }

    public void removeSpeaker(String speakerId) {
        speakers.removeIf(speaker -> speaker.getId().equals(speakerId));
    }

    public String generateCertificateForAttendee(String sessionId, String attendeeId) {
        if (!sessions.containsKey(sessionId)) {
            throw new IllegalArgumentException("Session with this ID does not exist.");
        }
        Session session = sessions.get(sessionId);
        if (!session.getAttendees().contains(attendeeId)) {
            throw new IllegalArgumentException("Attendee is not registered for this session.");
        }
        return "Certificate for Attendee ID: " + attendeeId + " for session: " + session.getName();
    }
}

import java.time.LocalDate;
import java.util.*;

public class Conference {
    private String id;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Set<Attendee> attendees;
    private Set<Speaker> speakers;
    private final SessionService sessionService; // Use SessionService for session management

    // Constructor
    public Conference(String id, String name, String description, LocalDate startDate, LocalDate endDate, SessionService sessionService) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.attendees = new HashSet<>();
        this.speakers = new HashSet<>();
        this.sessionService = sessionService;
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

    public Set<Attendee> getAttendees() {
        return attendees;
    }

    public Set<Speaker> getSpeakers() {
        return speakers;
    }

    public List<Session> getSessions() {
        return sessionService.getAllSessions();
    }

    // New getter for SessionService
    public SessionService getSessionService() {
        return this.sessionService;
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
        try {
            sessionService.createSession(
                    session.getName(),
                    session.getStartTime(),
                    session.getEndTime(),
                    session.getStatus()
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Error adding session: " + e.getMessage());
        }
    }

    public void modifySession(String sessionId, Session updatedSession) {
        // Use SessionService to update session details
        sessionService.updateSessionDetails(
                sessionId,
                updatedSession.getName(),
                updatedSession.getStartTime(),
                updatedSession.getEndTime(),
                updatedSession.getStatus()
        );
    }

    public void removeSession(String sessionId) {
        sessionService.deleteSession(sessionId);
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
        Session session = sessionService.findSessionById(sessionId);
        if (session == null) {
            throw new IllegalArgumentException("Session with this ID does not exist.");
        }
        if (!session.getAttendees().contains(attendeeId)) {
            throw new IllegalArgumentException("Attendee is not registered for this session.");
        }
        return "Certificate for Attendee ID: " + attendeeId + " for session: " + session.getName();
    }
}

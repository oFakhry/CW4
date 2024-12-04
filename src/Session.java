import java.time.LocalDateTime;
import java.util.*;

public class Session {
    private String id;
    private String name;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private SessionStatus status;
    private Set<String> attendees; // Storing attendee IDs for easy lookup

    // Constructor
    public Session(String id, String name, LocalDateTime startTime, LocalDateTime endTime, SessionStatus status) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.attendees = new HashSet<>();
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public SessionStatus getStatus() {
        return status;
    }

    public Set<String> getAttendees() {
        return attendees;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
    }

    // Business Logic
    public void registerAttendee(String attendeeId) {
        if (attendees.contains(attendeeId)) {
            throw new IllegalArgumentException("Attendee already registered for this session.");
        }
        attendees.add(attendeeId);
    }

    public void deregisterAttendee(String attendeeId) {
        if (!attendees.contains(attendeeId)) {
            throw new IllegalArgumentException("Attendee not registered for this session.");
        }
        attendees.remove(attendeeId);
    }
}

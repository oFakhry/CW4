import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Session {
    private String id;
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endTime;

    private SessionStatus status;
    private Set<String> attendees;

    public Session() {
        this.attendees = new HashSet<>();
    }

    public Session(String id, String name, LocalDateTime startTime, LocalDateTime endTime, SessionStatus status) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.attendees = new HashSet<>();
    }

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

    public void registerAttendee(String attendeeId) {
        if (attendees.contains(attendeeId)) {
            throw new IllegalArgumentException("Attendee already registered.");
        }
        attendees.add(attendeeId);
    }

    public void deregisterAttendee(String attendeeId) {
        if (!attendees.contains(attendeeId)) {
            throw new IllegalArgumentException("Attendee not registered.");
        }
        attendees.remove(attendeeId);
    }

    @Override
    public String toString() {
        return "Session{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", status=" + status +
                ", attendees=" + attendees +
                '}';
    }
}

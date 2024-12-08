import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Session {
    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("startTime")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startTime;

    @JsonProperty("endTime")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endTime;

    @JsonProperty("status")
    private SessionStatus status;

    @JsonProperty("attendees")
    private Set<String> attendees;

    @JsonProperty("speakers")
    private Set<Speaker> speakers;  // Set of speakers assigned to the session

    @JsonProperty("feedback")
    private Set<Feedback> feedbacks;  // Set of feedback for the session

    public Session() {
        this.attendees = new HashSet<>();
        this.speakers = new HashSet<>();
        this.feedbacks = new HashSet<>();
    }

    public Session(String id, String name, LocalDateTime startTime, LocalDateTime endTime, SessionStatus status) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.attendees = new HashSet<>();
        this.speakers = new HashSet<>();
        this.feedbacks = new HashSet<>();
    }

    // Method to add a speaker to the session
    public void addSpeaker(Speaker speaker) {
        if (speaker != null) {
            this.speakers.add(speaker);
        }
    }

    // Method to add feedback from an attendee
    public void addFeedback(Attendee attendee, String feedback) {
        if (attendee != null && feedback != null && !feedback.isEmpty()) {
            this.feedbacks.add(new Feedback(attendee, feedback));
        }
    }

    // Getters and Setters
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

    public Set<Speaker> getSpeakers() {
        return speakers;
    }

    public Set<Feedback> getFeedbacks() {
        return feedbacks;
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

    @Override
    public String toString() {
        return "Session{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", status=" + status +
                ", attendees=" + attendees +
                ", speakers=" + speakers +
                ", feedbacks=" + feedbacks +
                '}';
    }
}

public class Feedback {
    private String id; // Unique ID for the feedback
    private String attendeeId; // Attendee who gave the feedback
    private String conferenceId; // Conference the feedback is for
    private String comments; // The actual feedback text

    // Constructor
    public Feedback(String id, String attendeeId, String conferenceId, String comments) {
        this.id = id;
        this.attendeeId = attendeeId;
        this.conferenceId = conferenceId;
        this.comments = comments;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAttendeeId() {
        return attendeeId;
    }

    public void setAttendeeId(String attendeeId) {
        this.attendeeId = attendeeId;
    }

    public String getConferenceId() {
        return conferenceId;
    }

    public void setConferenceId(String conferenceId) {
        this.conferenceId = conferenceId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    // ToString Method for Readable Output
    @Override
    public String toString() {
        return "Feedback{" +
                "id='" + id + '\'' +
                ", attendeeId='" + attendeeId + '\'' +
                ", conferenceId='" + conferenceId + '\'' +
                ", comments='" + comments + '\'' +
                '}';
    }
}

public class Feedback {
    private Attendee attendee;
    private String feedback;

    public Feedback(Attendee attendee, String feedback) {
        this.attendee = attendee;
        this.feedback = feedback;
    }

    // Getters and Setters
    public Attendee getAttendee() {
        return attendee;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setAttendee(Attendee attendee) {
        this.attendee = attendee;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "attendee=" + attendee +
                ", feedback='" + feedback + '\'' +
                '}';
    }
}

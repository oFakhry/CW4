import java.util.List;

public class AttendeeService {
    private final AttendeeRepository attendeeRepository;
    private final FeedbackRepository feedbackRepository;

    public AttendeeService(AttendeeRepository attendeeRepository, FeedbackRepository feedbackRepository) {
        this.attendeeRepository = attendeeRepository;
        this.feedbackRepository = feedbackRepository;
    }

    // Register an attendee for a conference
    public void registerForConference(String attendeeId, Conference conference, String password) {
        Attendee attendee = attendeeRepository.findById(attendeeId);
        if (attendee != null) {
            attendee.registerForConference(conference);
            attendeeRepository.save(attendee); // Save with the password
        } else {
            throw new IllegalArgumentException("Attendee not found.");
        }
    }


    // Submit feedback for a conference
    public void submitFeedback(String attendeeId, String conferenceId, String feedbackText) {
        Attendee attendee = attendeeRepository.findById(attendeeId);
        if (attendee != null) {
            // Ensure the attendee is registered for the conference before allowing feedback submission
            boolean isRegistered = attendee.getRegisteredConferences().stream()
                    .anyMatch(conf -> conf.getId().equals(conferenceId));
            if (isRegistered) {
                feedbackRepository.addFeedback(attendeeId, conferenceId, feedbackText);
            } else {
                throw new IllegalArgumentException("Attendee is not registered for this conference.");
            }
        } else {
            throw new IllegalArgumentException("Attendee not found.");
        }
    }

    // Fetch registered conferences for an attendee
    public List<Conference> getRegisteredConferences(String attendeeId) {
        Attendee attendee = attendeeRepository.findById(attendeeId);
        if (attendee != null) {
            return attendee.getRegisteredConferences();
        }
        throw new IllegalArgumentException("Attendee not found.");
    }

    // Retrieve all feedback for an attendee
    public List<Feedback> getFeedbackForAttendee(String attendeeId) {
        Attendee attendee = attendeeRepository.findById(attendeeId);
        if (attendee != null) {
            return feedbackRepository.findByAttendeeId(attendeeId);
        }
        throw new IllegalArgumentException("Attendee not found.");
    }
}

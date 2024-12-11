public class LoginAndRegistration {
    private final AttendeeRepository attendeeRepository;
    private final OrganizerRepository organizerRepository;
    private final SpeakerRepository speakerRepository;
    private final FeedbackRepository feedbackRepository; // Add FeedbackRepository field
    private final AttendeeService attendeeService;

    public LoginAndRegistration(AttendeeRepository attendeeRepository, OrganizerRepository organizerRepository, SpeakerRepository speakerRepository, FeedbackRepository feedbackRepository) {
        this.attendeeRepository = attendeeRepository;
        this.organizerRepository = organizerRepository;
        this.speakerRepository = speakerRepository;
        this.feedbackRepository = feedbackRepository; // Initialize FeedbackRepository
        this.attendeeService = new AttendeeService(attendeeRepository, feedbackRepository); // Initialize attendee service
    }

    // Register a new user
    public void register(String name, String email, String password, String role) {
        String id = generateUniqueId();

        switch (role.toLowerCase()) {
            case "attendee" -> {
                if (attendeeRepository.findById(id) != null) {
                    throw new IllegalArgumentException("User with this ID already exists.");
                }
                Attendee attendee = new Attendee(id, name, email, password);
                attendeeRepository.save(attendee);
                System.out.println("Attendee registered successfully.");
            }
            case "organizer" -> {
                if (organizerRepository.findById(id) != null) {
                    throw new IllegalArgumentException("User with this ID already exists.");
                }
                Organizer organizer = new Organizer(id, name, email, password);
                organizerRepository.save(organizer, password);
                System.out.println("Organizer registered successfully.");
            }
            case "speaker" -> {
                if (speakerRepository.findById(id) != null) {
                    throw new IllegalArgumentException("User with this ID already exists.");
                }
                Speaker speaker = new Speaker(id, name, email, "", password); // Default empty bio
                speakerRepository.save(speaker);
                System.out.println("Speaker registered successfully.");
            }
            default -> throw new IllegalArgumentException("Invalid role. Please choose Attendee, Organizer, or Speaker.");
        }
    }

    // Login a user
    public People login(String email, String password, String role) {
        switch (role.toLowerCase()) {
            case "attendee" -> {
                Attendee attendee = attendeeRepository.findByEmail(email);
                if (attendee != null && password.equals(attendee.getPassword())) {
                    System.out.println("Attendee login successful.");
                    return attendee;
                }
            }
            case "organizer" -> {
                Organizer organizer = organizerRepository.findByEmail(email);
                if (organizer != null && password.equals(organizer.getPassword())) {
                    System.out.println("Organizer login successful.");
                    return organizer;
                }
            }
            case "speaker" -> {
                Speaker speaker = speakerRepository.findByEmail(email);
                if (speaker != null && password.equals(speaker.getPassword())) {
                    System.out.println("Speaker login successful.");
                    return speaker;
                }
            }
        }
        throw new IllegalArgumentException("Invalid email, password, or role.");
    }

    public AttendeeService getAttendeeService() {
        return attendeeService;
    }

    // Add getFeedbackRepository() method
    public FeedbackRepository getFeedbackRepository() {
        return feedbackRepository;
    }

    // Generate a unique ID
    private String generateUniqueId() {
        return "USR-" + System.currentTimeMillis();
    }
}

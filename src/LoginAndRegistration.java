import java.util.Scanner;

public class LoginAndRegistration {
    private final AttendeeRepository attendeeRepository;
    private final OrganizerRepository organizerRepository;
    private final SpeakerRepository speakerRepository;

    public LoginAndRegistration(AttendeeRepository attendeeRepository, OrganizerRepository organizerRepository, SpeakerRepository speakerRepository) {
        this.attendeeRepository = attendeeRepository;
        this.organizerRepository = organizerRepository;
        this.speakerRepository = speakerRepository;
    }

    // Register a new user
    public void register(String name, String email, String password, String role) {
        String id = generateUniqueId();

        switch (role.toLowerCase()) {
            case "attendee" -> {
                if (attendeeRepository.findById(id) != null) {
                    throw new IllegalArgumentException("User with this ID already exists.");
                }
                Attendee attendee = new Attendee(id, name, email);
                attendeeRepository.save(attendee);
                System.out.println("Attendee registered successfully.");
            }
            case "organizer" -> {
                if (organizerRepository.findById(id) != null) {
                    throw new IllegalArgumentException("User with this ID already exists.");
                }
                Organizer organizer = new Organizer(id, name, email);
                organizerRepository.save(organizer);
                System.out.println("Organizer registered successfully.");
            }
            case "speaker" -> {
                if (speakerRepository.findById(id) != null) {
                    throw new IllegalArgumentException("User with this ID already exists.");
                }
                Speaker speaker = new Speaker(id, name, email, ""); // Pass an empty bio or a default bio
                speakerRepository.save(speaker, password); // Save the speaker with the provided password
                System.out.println("Speaker registered successfully.");
            }
            default ->
                    throw new IllegalArgumentException("Invalid role. Please choose either Attendee, Organizer, or Speaker.");
        }
    }


    // Login a user
    public People login(String email, String password, String role) {
        switch (role.toLowerCase()) {
            case "attendee" -> {
                Attendee attendee = attendeeRepository.findAll().stream()
                        .filter(a -> a.getEmail().equals(email))
                        .findFirst()
                        .orElse(null);
                if (attendee != null) {
                    System.out.println("Attendee login successful.");
                    return attendee;
                }
            }
            case "organizer" -> {
                Organizer organizer = organizerRepository.findAll().stream()
                        .filter(o -> o.getEmail().equals(email))
                        .findFirst()
                        .orElse(null);
                if (organizer != null) {
                    System.out.println("Organizer login successful.");
                    return organizer;
                }
            }
            case "speaker" -> {
                Speaker speaker = speakerRepository.findAll().stream()
                        .filter(s -> s.getEmail().equals(email) && password.equals(speakerRepository.getPasswordById(s.getId())))
                        .findFirst()
                        .orElse(null);
                if (speaker != null) {
                    System.out.println("Speaker login successful.");
                    return speaker;
                }
            }
        }
        throw new IllegalArgumentException("Invalid email, password, or role.");
    }

    // Generate a unique ID
    private String generateUniqueId() {
        return "USR-" + System.currentTimeMillis();
    }
}
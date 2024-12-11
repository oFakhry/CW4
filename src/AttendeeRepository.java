import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AttendeeRepository {
    private final File file;
    private final ObjectMapper objectMapper;
    private List<Attendee> attendees;

    public AttendeeRepository(String filePath) {
        this.file = new File(filePath);
        this.objectMapper = new ObjectMapper();
        this.attendees = loadAttendeesFromFile();
    }

    // Save an attendee
    public void save(Attendee attendee) {
        attendees.removeIf(existingAttendee -> existingAttendee.getId().equals(attendee.getId()));
        attendees.add(attendee);
        saveAttendeesToFile();
    }

    // Find an attendee by ID
    public Attendee findById(String id) {
        return attendees.stream()
                .filter(attendee -> attendee.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Attendee findByEmail(String email) {
        return attendees.stream()
                .filter(s -> s.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    public String getPasswordByEmail(String email) {
        Attendee attendee = findByEmail(email);
        return attendee != null ? attendee.getPassword() : null;
    }


    // Get all attendees
    public List<Attendee> findAll() {
        return new ArrayList<>(attendees);
    }

    // Delete an attendee by ID
    public void deleteById(String id) {
        attendees.removeIf(attendee -> attendee.getId().equals(id));
        saveAttendeesToFile();
    }

    // Load attendees from the JSON file
    private List<Attendee> loadAttendeesFromFile() {
        try {
            if (!file.exists()) {
                file.createNewFile();
                return new ArrayList<>();
            }
            return objectMapper.readValue(file, new TypeReference<List<Attendee>>() {});
        } catch (IOException e) {
            System.err.println("Error loading attendees from file: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Save attendees to the JSON file
    private void saveAttendeesToFile() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, attendees);
        } catch (IOException e) {
            System.err.println("Error saving attendees to file: " + e.getMessage());
        }
    }
}

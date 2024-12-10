import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.File;
import java.io.FileWriter;
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

    // Save an attendee to the file
    public void save(Attendee attendee) {
        // Remove existing attendee with the same ID
        attendees.removeIf(existingAttendee -> existingAttendee.getId().equals(attendee.getId()));
        // Add the updated/new attendee
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
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Save attendees to the JSON file
    private void saveAttendeesToFile() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, attendees);
        } catch (IOException e) {
            System.err.println("Error saving attendees to file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Write all attendee information to a file in a human-readable format
    public void writeAllAttendeesToFile(String outputFilePath) {
        File outputFile = new File(outputFilePath);

        try (var writer = new FileWriter(outputFile)) {
            writer.write("Attendee Information:\n");
            for (Attendee attendee : attendees) {
                writer.write(formatAttendeeDetails(attendee));
                writer.write("\n--------------------------------------------\n");
            }
            System.out.println("All attendees written to file: " + outputFilePath);
        } catch (IOException e) {
            System.err.println("Error writing all attendees to file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Helper method to format attendee details
    private String formatAttendeeDetails(Attendee attendee) {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(attendee.getId()).append("\n");
        sb.append("Name: ").append(attendee.getName()).append("\n");
        sb.append("Email: ").append(attendee.getEmail()).append("\n");
        sb.append("Registered Conferences: ");
        sb.append(attendee.getRegisteredConferences().stream()
                .map(Conference::getName)
                .toList()).append("\n");
        return sb.toString();
    }
}

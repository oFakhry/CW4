import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SessionRepository {
    private final File file;
    private final ObjectMapper objectMapper;
    private List<Session> sessions;

    public SessionRepository(String filePath) {
        this.file = new File(filePath);
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.sessions = loadSessionsFromFile();
    }

    // Save a session to the file
    public void save(Session session) {
        // Remove existing session with the same ID
        sessions.removeIf(existingSession -> existingSession.getId().equals(session.getId()));
        // Add the updated/new session
        sessions.add(session);
        saveSessionsToFile();
    }

    // Find a session by ID
    public Session findById(String id) {
        return sessions.stream()
                .filter(session -> session.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // Get all sessions
    public List<Session> findAll() {
        return new ArrayList<>(sessions);
    }

    // Delete a session by ID
    public void deleteById(String id) {
        sessions.removeIf(session -> session.getId().equals(id));
        saveSessionsToFile();
    }

    // Load sessions from the JSON file
    private List<Session> loadSessionsFromFile() {
        try {
            if (!file.exists()) {
                file.createNewFile();
                return new ArrayList<>();
            }
            return objectMapper.readValue(file, new TypeReference<List<Session>>() {});
        } catch (IOException e) {
            System.err.println("Error loading sessions from file: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Save sessions to the JSON file
    private void saveSessionsToFile() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, sessions);
        } catch (IOException e) {
            System.err.println("Error saving sessions to file: " + e.getMessage());
            e.printStackTrace();
        }
    }


    // Write all session information to a file in a human-readable format
    public void writeAllSessionsToFile(String outputFilePath) {
        File outputFile = new File(outputFilePath);

        try (FileWriter writer = new FileWriter(outputFile)) {
            writer.write("Session Information:\n");
            for (Session session : sessions) {
                writer.write(formatSessionDetails(session));
                writer.write("\n--------------------------------------------\n");
            }
            System.out.println("All sessions written to file: " + outputFilePath);
        } catch (IOException e) {
            System.err.println("Error writing all sessions to file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Helper method to format session details
    private String formatSessionDetails(Session session) {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(session.getId()).append("\n");
        sb.append("Name: ").append(session.getName()).append("\n");
        sb.append("Start Time: ").append(session.getStartTime()).append("\n");
        sb.append("End Time: ").append(session.getEndTime()).append("\n");
        sb.append("Status: ").append(session.getStatus()).append("\n");
        sb.append("Attendees: ").append(String.join(", ", session.getAttendees())).append("\n");
        return sb.toString();
    }
}

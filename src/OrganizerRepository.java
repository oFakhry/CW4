import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class OrganizerRepository {

    private final File file;
    private final ObjectMapper objectMapper;
    private List<Organizer> organizers;
    private Map<String, String> passwords;

    // Constructor
    public OrganizerRepository(String filePath) {
        this.file = new File(filePath);
        this.objectMapper = new ObjectMapper();
        // Register modules and configure for Java time
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        this.organizers = loadOrganizersFromFile();
        this.passwords = new HashMap<>();
    }

    // Save an organizer to the file
    public void save(Organizer organizer, String password) {
        organizers.removeIf(existingOrganizer -> existingOrganizer.getId().equals(organizer.getId()));
        organizers.add(organizer);
        passwords.put(organizer.getId(), password);
        saveOrganizersToFile();
    }

    public Organizer findByEmail(String email) {
        return organizers.stream()
                .filter(s -> s.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    public String getPasswordByEmail(String email) {
        Organizer organizer = findByEmail(email);
        return organizer != null ? organizer.getPassword() : null;
    }

    // Find an organizer by ID
    public Organizer findById(String id) {
        return organizers.stream()
                .filter(organizer -> organizer.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // Get all organizers
    public List<Organizer> findAll() {
        return new ArrayList<>(organizers);
    }

    // Delete an organizer by ID
    public void deleteById(String id) {
        organizers.removeIf(organizer -> organizer.getId().equals(id));
        saveOrganizersToFile();
    }

    // Load organizers from the JSON file
    private List<Organizer> loadOrganizersFromFile() {
        try {
            if (!file.exists()) {
                file.createNewFile();
                return new ArrayList<>();
            }
            return objectMapper.readValue(file, new TypeReference<List<Organizer>>() {});
        } catch (IOException e) {
            System.err.println("Error loading organizers from file: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Save organizers to the JSON file using atomic write
    private void saveOrganizersToFile() {
        File tempFile = new File(file.getAbsolutePath() + ".tmp");
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(tempFile, organizers);
            if (file.exists()) {
                file.delete();
            }
            tempFile.renameTo(file);
        } catch (IOException e) {
            System.err.println("Error saving organizers to file: " + e.getMessage());
            e.printStackTrace();
            // If something goes wrong, tempFile remains. You could delete it if desired.
        }
    }
}

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OrganizerRepository {

    private final File file;
    private final ObjectMapper objectMapper;
    private List<Organizer> organizers;

    // Constructor
    public OrganizerRepository(String filePath) {
        this.file = new File(filePath);
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.organizers = loadOrganizersFromFile();
    }

    // Save an organizer to the file
    public void save(Organizer organizer) {
        // Remove existing organizer with the same ID
        organizers.removeIf(existingOrganizer -> existingOrganizer.getId() == organizer.getId());
        // Add the updated/new organizer
        organizers.add(organizer);
        saveOrganizersToFile();
    }

    // Find an organizer by ID
    public Organizer findById(String id) {
        return organizers.stream()
                .filter(organizer -> organizer.getId().equals(id))  // Compare using .equals() for String
                .findFirst()
                .orElse(null);
    }


    // Get all organizers
    public List<Organizer> findAll() {
        return new ArrayList<>(organizers);
    }

    // Delete an organizer by ID
    public void deleteById(String id) {
        organizers.removeIf(organizer -> organizer.getId().equals(id));  // Compare using .equals() for String
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

    // Save organizers to the JSON file
    private void saveOrganizersToFile() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, organizers);
        } catch (IOException e) {
            System.err.println("Error saving organizers to file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Write all organizer information to a file in a human-readable format
    public void writeAllOrganizersToFile(String outputFilePath) {
        File outputFile = new File(outputFilePath);

        try (FileWriter writer = new FileWriter(outputFile)) {
            writer.write("Organizer Information:\n");
            for (Organizer organizer : organizers) {
                writer.write(formatOrganizerDetails(organizer));
                writer.write("\n--------------------------------------------\n");
            }
            System.out.println("All organizers written to file: " + outputFilePath);
        } catch (IOException e) {
            System.err.println("Error writing all organizers to file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Helper method to format organizer details
    private String formatOrganizerDetails(Organizer organizer) {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(organizer.getId()).append("\n");
        sb.append("Name: ").append(organizer.getName()).append("\n");
        sb.append("Email: ").append(organizer.getEmail()).append("\n");
        sb.append("Speakers Registered: ").append(organizer.getRegisteredSpeakers().size()).append("\n");
        // Initialize People (Organizer is a subclass of People)
        People people = organizer;  // Since Organizer extends People, we can use the People class

        // Conference details: Use the viewConferenceDetails() method from People class
        sb.append("Conference Details: ");
        List<String> conferenceDetails = new ArrayList<>();
        for (Conference conference : organizer.getConferences()) {
            // Use the viewConferenceDetails() method to get the details for each conference
            conferenceDetails.add(people.viewConferenceDetails(conference));
        }

        // Join the conference details into one string
        sb.append(String.join("\n", conferenceDetails)).append("\n");
        return sb.toString();
    }
}

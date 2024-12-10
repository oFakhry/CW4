import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpeakerRepository {
    private final File file;
    private final ObjectMapper objectMapper;
    private List<Speaker> speakers;
    private Map<String, String> passwords; // Map of speaker ID to password


    public SpeakerRepository(String filePath) {
        this.file = new File(filePath);
        this.objectMapper = new ObjectMapper();
        this.speakers = loadSpeakersFromFile();
        this.passwords = new HashMap<>();

    }

    // Save a speaker with a password
    public void save(Speaker speaker, String password) {
        speakers.removeIf(existingSpeaker -> existingSpeaker.getId().equals(speaker.getId()));
        speakers.add(speaker);
        passwords.put(speaker.getId(), password);
        saveSpeakersToFile();
    }


    // Find a speaker by ID
    public Speaker findById(String id) {
        return speakers.stream()
                .filter(speaker -> speaker.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public String getPasswordById(String id) {
        return passwords.get(id);
    }

    // Get all speakers
    public List<Speaker> findAll() {
        return new ArrayList<>(speakers);
    }

    // Delete a speaker by ID
    public void deleteById(String id) {
        speakers.removeIf(speaker -> speaker.getId().equals(id));
        saveSpeakersToFile();
    }

    // Load speakers from the JSON file
    private List<Speaker> loadSpeakersFromFile() {
        try {
            if (!file.exists()) {
                file.createNewFile();
                return new ArrayList<>();
            }
            return objectMapper.readValue(file, new TypeReference<List<Speaker>>() {});
        } catch (IOException e) {
            System.err.println("Error loading speakers from file: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Save speakers to the JSON file
    private void saveSpeakersToFile() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, speakers);
        } catch (IOException e) {
            System.err.println("Error saving speakers to file: " + e.getMessage());
        }
    }
}

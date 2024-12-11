import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConferenceRepository {
    private final String filePath;
    private final ObjectMapper objectMapper;

    public ConferenceRepository(String filePath) {
        this.filePath = filePath;
        this.objectMapper = new ObjectMapper();
    }

    // Load conferences from the JSON file
    public List<Conference> loadConferencesFromFile() {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            return objectMapper.readValue(file, new TypeReference<List<Conference>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Save conferences to the JSON file
    public void saveConferencesToFile(List<Conference> conferences) {
        try {
            objectMapper.writeValue(new File(filePath), conferences);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

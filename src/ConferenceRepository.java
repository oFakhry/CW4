import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

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
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
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

    // Save conferences to the JSON file using atomic write
    public void saveConferencesToFile(List<Conference> conferences) {
        File file = new File(filePath);
        File tempFile = new File(filePath + ".tmp");
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(tempFile, conferences);
            if (file.exists()) {
                file.delete();
            }
            tempFile.renameTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

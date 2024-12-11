import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FeedbackRepository {
    private final File file;
    private final ObjectMapper objectMapper;
    private List<Feedback> feedbackList;

    public FeedbackRepository(String filePath) {
        this.file = new File(filePath);
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.feedbackList = loadFeedbackFromFile();
    }

    // Add feedback
    public void addFeedback(String attendeeId, String conferenceId, String feedbackText) {
        Feedback feedback = new Feedback(generateFeedbackId(), attendeeId, conferenceId, feedbackText);
        feedbackList.add(feedback);
        saveFeedbackToFile();
    }

    // Retrieve all feedback
    public List<Feedback> findAllFeedback() {
        return new ArrayList<>(feedbackList);
    }

    // Find feedback by attendee ID
    public List<Feedback> findByAttendeeId(String attendeeId) {
        List<Feedback> result = new ArrayList<>();
        for (Feedback feedback : feedbackList) {
            if (feedback.getAttendeeId().equals(attendeeId)) {
                result.add(feedback);
            }
        }
        return result;
    }

    // Load feedback from the JSON file
    private List<Feedback> loadFeedbackFromFile() {
        try {
            if (!file.exists()) {
                file.createNewFile();
                return new ArrayList<>();
            }
            return objectMapper.readValue(file, new TypeReference<List<Feedback>>() {});
        } catch (IOException e) {
            System.err.println("Error loading feedback from file: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Save feedback to the JSON file using atomic write
    private void saveFeedbackToFile() {
        File tempFile = new File(file.getAbsolutePath() + ".tmp");
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(tempFile, feedbackList);
            if (file.exists()) {
                file.delete();
            }
            tempFile.renameTo(file);
        } catch (IOException e) {
            System.err.println("Error saving feedback to file: " + e.getMessage());
        }
    }

    // Generate unique feedback ID
    private String generateFeedbackId() {
        return "F-" + (feedbackList.size() + 1);
    }
}

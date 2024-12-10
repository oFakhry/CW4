import java.util.List;

public class SpeakerService {
    private final SpeakerRepository speakerRepository;
    private final SessionRepository sessionRepository;

    public SpeakerService(SpeakerRepository speakerRepository, SessionRepository sessionRepository) {
        this.speakerRepository = speakerRepository;
        this.sessionRepository = sessionRepository;
    }

    // Add or update a speaker
    public void addOrUpdateSpeaker(Speaker speaker, String password) {
        speakerRepository.save(speaker, password); // Pass the password along when saving the speaker
    }

    // Find a speaker by ID
    public Speaker findSpeakerById(String id) {
        return speakerRepository.findById(id);
    }

    // Get all speakers
    public List<Speaker> getAllSpeakers() {
        return speakerRepository.findAll();
    }

    // Delete a speaker by ID
    public void deleteSpeakerById(String id) {
        speakerRepository.deleteById(id);
    }

    // View sessions assigned to a speaker
    public String viewSpeakerSessions(String speakerId) {
        Speaker speaker = findSpeakerById(speakerId);
        if (speaker == null) {
            throw new IllegalArgumentException("Speaker not found with ID: " + speakerId);
        }
        return speaker.viewSessionsForSpeaker(speaker, sessionRepository);
    }

    // Create or update a speaker profile
    public void createOrUpdateSpeakerProfile(String speakerId, String name, String bio, String password) {
        Speaker speaker = findSpeakerById(speakerId);
        if (speaker == null) {
            throw new IllegalArgumentException("Speaker not found with ID: " + speakerId);
        }
        speaker.createOrUpdateSpeakerProfile(speaker, name, bio); // Update profile info
        speakerRepository.save(speaker, password); // Save the updated speaker with password
    }
}

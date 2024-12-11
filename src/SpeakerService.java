import java.util.List;

public class SpeakerService {
    private final SpeakerRepository speakerRepository;
    private final SessionRepository sessionRepository;

    public SpeakerService(SpeakerRepository speakerRepository, SessionRepository sessionRepository) {
        this.speakerRepository = speakerRepository;
        this.sessionRepository = sessionRepository;
        initializeIdCounter();
    }

    // Add or update a speaker
    public void addOrUpdateSpeaker(Speaker speaker) {
        speakerRepository.save(speaker);
    }

    public void deleteSpeaker(Speaker speaker) {
        if (speaker == null) {
            throw new IllegalArgumentException("Speaker cannot be null.");
        }

        String speakerId = speaker.getId();
        if (speakerRepository.findById(speakerId) == null) {
            throw new IllegalArgumentException("Speaker with ID " + speakerId + " does not exist.");
        }

        speakerRepository.deleteById(speakerId);
    }

    // Find a speaker by ID
    public Speaker findSpeakerById(String id) {
        return speakerRepository.findById(id);
    }

    public Speaker createSpeaker(String name, String email, String bio, String password) {
        String speakerId = String.valueOf(IDGenerator.generateSpeakerId());
        Speaker newSpeaker = new Speaker(speakerId, name, email, bio, password);
        speakerRepository.save(newSpeaker);
        return newSpeaker;
    }

    private void initializeIdCounter() {
        List<Speaker> speakers = speakerRepository.findAll();
        int maxId = 0;
        for (Speaker sp : speakers) {
            try {
                int id = Integer.parseInt(sp.getId());
                if (id > maxId) {
                    maxId = id;
                }
            } catch (NumberFormatException e) {
                // Handle non-integer IDs if any
            }
        }
        IDGenerator.initializeSpeakerId(maxId);
    }


    // Find a speaker by name
    public Speaker getSpeakerByName(String name) {
        return speakerRepository.findAll().stream()
                .filter(speaker -> speaker.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Speaker not found with name: " + name));
    }

    // Get all speakers
    public List<Speaker> getAllSpeakers() {
        return speakerRepository.findAll();
    }

    public Speaker getSpeakerById(String id) {
        return speakerRepository.findById(id);
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
    public void createOrUpdateSpeakerProfile(String speakerId, String name, String bio) {
        Speaker speaker = findSpeakerById(speakerId);
        if (speaker == null) {
            throw new IllegalArgumentException("Speaker not found with ID: " + speakerId);
        }
        speaker.createOrUpdateSpeakerProfile(speaker, name, bio); // Update profile info
        speakerRepository.save(speaker); // Save the updated speaker
    }

    // Save all speakers to the file
    public void saveSpeakersToFile(List<Speaker> speakers) {
        speakerRepository.saveAll(speakers);
    }
}


import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ConferenceService {
    private final SessionService sessionService;
    private final ConferenceRepository conferenceRepository;

    public ConferenceService(SessionService sessionService, ConferenceRepository conferenceRepository) {
        this.sessionService = sessionService;
        this.conferenceRepository = conferenceRepository;
        initializeIdCounter();
    }

    private void initializeIdCounter() {
        List<Conference> conferences = conferenceRepository.loadConferencesFromFile();
        int maxId = 0;
        for (Conference conf : conferences) {
            try {
                int id = Integer.parseInt(conf.getId());
                if (id > maxId) {
                    maxId = id;
                }
            } catch (NumberFormatException e) {
                // Handle non-integer IDs if any
            }
        }
        IDGenerator.initializeConferenceId(maxId);
    }

    public Conference createConference(String name, String description, LocalDate startDate, LocalDate endDate) {
        String conferenceId = String.valueOf(IDGenerator.generateConferenceId());
        Conference conference = new Conference(conferenceId, name, description, startDate, endDate, sessionService);
        List<Conference> allConferences = getConferences();
        allConferences.add(conference);
        saveConferencesToFile(allConferences);
        return conference;
    }

    public void updateConference(String conferenceId, String name, String description, LocalDate startDate, LocalDate endDate) {
        List<Conference> allConferences = getConferences();
        for (Conference c : allConferences) {
            if (c.getId().equals(conferenceId)) {
                c.setName(name);
                c.setDescription(description);
                c.setStartDate(startDate);
                c.setEndDate(endDate);
                break;
            }
        }
        saveConferencesToFile(allConferences);
    }

    public void deleteConference(String conferenceId) {
        List<Conference> allConferences = getConferences().stream()
                .filter(c -> !c.getId().equals(conferenceId))
                .collect(Collectors.toList());
        saveConferencesToFile(allConferences);
    }

    public List<Conference> getConferences() {
        return conferenceRepository.loadConferencesFromFile();
    }

    public void saveConferencesToFile(List<Conference> conferences) {
        conferenceRepository.saveConferencesToFile(conferences);
    }

    public SessionService getSessionService() {
        return sessionService;
    }
}

import java.util.List;

public class OrganizerService {
    private final OrganizerRepository organizerRepository;

    public OrganizerService(OrganizerRepository organizerRepository) {
        this.organizerRepository = organizerRepository;
        initializeIdCounter();
    }

    private void initializeIdCounter() {
        List<Organizer> organizers = organizerRepository.findAll();
        int maxId = 0;
        for (Organizer org : organizers) {
            try {
                int id = Integer.parseInt(org.getId());
                if (id > maxId) {
                    maxId = id;
                }
            } catch (NumberFormatException e) {
                // Handle non-integer IDs if any
            }
        }
        IDGenerator.initializeOrganizerId(maxId);
    }

    public Organizer createNewOrganizer(String name, String email, String password) {
        String generatedId = String.valueOf(IDGenerator.generateOrganizerId());
        Organizer newOrganizer = new Organizer(generatedId, name, email, password);
        createOrUpdateOrganizer(newOrganizer, password);
        return newOrganizer;
    }

    public void createOrUpdateOrganizer(Organizer organizer, String password) {
        organizerRepository.save(organizer, password);
    }

    public Organizer getOrganizerById(String id) {
        return organizerRepository.findById(id);
    }

    public List<Organizer> getAllOrganizers() {
        return organizerRepository.findAll();
    }

    public void deleteOrganizer(String id) {
        organizerRepository.deleteById(id);
    }
}

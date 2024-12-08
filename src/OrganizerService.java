import java.util.List;

public class OrganizerService {
    private final OrganizerRepository organizerRepository;

    public OrganizerService(OrganizerRepository organizerRepository) {
        this.organizerRepository = organizerRepository;
    }

    // Create or update an organizer
    public void createOrUpdateOrganizer(Organizer organizer) {
        organizerRepository.save(organizer);
    }

    // Get an organizer by ID
    public Organizer getOrganizerById(String id) {
        return organizerRepository.findById(id);
    }

    // Delete an organizer by ID
    public void deleteOrganizer(String id) {
        organizerRepository.deleteById(id);
    }

    // Get all organizers
    public List<Organizer> getAllOrganizers() {
        return organizerRepository.findAll();
    }
}

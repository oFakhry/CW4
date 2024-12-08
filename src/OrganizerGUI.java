import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class OrganizerGUI {
    private final OrganizerService organizerService;

    public OrganizerGUI(OrganizerService organizerService) {
        this.organizerService = organizerService;
        initialize();
    }

    private void initialize() {
        JFrame frame = new JFrame("Organizer Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));

        JLabel organizerIdLabel = new JLabel("Organizer ID:");
        JTextField organizerIdField = new JTextField();
        JButton loadButton = new JButton("Load Organizer");

        JTextArea outputArea = new JTextArea(10, 50);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        // Create/Update Organizer Section
        JLabel newOrganizerNameLabel = new JLabel("New Organizer Name:");
        JTextField newOrganizerNameField = new JTextField();
        JLabel newOrganizerEmailLabel = new JLabel("New Organizer Email:");
        JTextField newOrganizerEmailField = new JTextField();
        JButton createOrUpdateButton = new JButton("Create/Update Organizer");

        // Delete Organizer Section
        JButton deleteButton = new JButton("Delete Organizer");

        // Manage Conferences Section
        JButton manageConferencesButton = new JButton("Manage Conferences");

        // Manage Speakers Section
        JButton manageSpeakersButton = new JButton("Manage Speakers");

        // Manage Session Section
        JButton assignSpeakerToSessionButton = new JButton("Assign Speaker to Session");

        panel.add(organizerIdLabel);
        panel.add(organizerIdField);
        panel.add(loadButton);

        panel.add(newOrganizerNameLabel);
        panel.add(newOrganizerNameField);
        panel.add(newOrganizerEmailLabel);
        panel.add(newOrganizerEmailField);
        panel.add(createOrUpdateButton);

        panel.add(deleteButton);
        panel.add(manageConferencesButton);
        panel.add(manageSpeakersButton);
        panel.add(assignSpeakerToSessionButton);

        frame.add(panel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Action Listeners
        loadButton.addActionListener(e -> handleLoadOrganizer(organizerIdField, outputArea));

        createOrUpdateButton.addActionListener(e -> handleCreateOrUpdateOrganizer(newOrganizerNameField, newOrganizerEmailField, outputArea));

        deleteButton.addActionListener(e -> handleDeleteOrganizer(organizerIdField, outputArea));

        manageConferencesButton.addActionListener(e -> handleManageConferences(organizerIdField, outputArea));

        manageSpeakersButton.addActionListener(e -> handleManageSpeakers(organizerIdField, outputArea));

        assignSpeakerToSessionButton.addActionListener(e -> handleAssignSpeakerToSession(organizerIdField, outputArea));

        frame.setVisible(true);
    }

    private void handleLoadOrganizer(JTextField organizerIdField, JTextArea outputArea) {
        try {
            String organizerId = organizerIdField.getText();
            Organizer organizer = organizerService.getOrganizerById(organizerId);
            if (organizer != null) {
                outputArea.setText(""); // Clear previous output
                outputArea.append("Organizer Details:\n");
                outputArea.append("Name: " + organizer.getName() + "\n");
                outputArea.append("Email: " + organizer.getEmail() + "\n");
                outputArea.append("Conferences: \n");
                for (Conference conference : organizer.getConferences()) {
                    outputArea.append(conference.getName() + " (" + conference.getStartDate() + " - " + conference.getEndDate() + ")\n");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Organizer not found", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleCreateOrUpdateOrganizer(JTextField nameField, JTextField emailField, JTextArea outputArea) {
        try {
            String name = nameField.getText();
            String email = emailField.getText();
            if (name.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Name and Email cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Organizer newOrganizer = new Organizer("new-id", name, email);
            organizerService.createOrUpdateOrganizer(newOrganizer);

            outputArea.setText("Organizer created/updated successfully.\n");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDeleteOrganizer(JTextField organizerIdField, JTextArea outputArea) {
        try {
            String organizerId = organizerIdField.getText();
            organizerService.deleteOrganizer(organizerId);
            outputArea.setText("Organizer deleted successfully.\n");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleManageConferences(JTextField organizerIdField, JTextArea outputArea) {
        // Placeholder implementation
        String organizerId = organizerIdField.getText();
        Organizer organizer = organizerService.getOrganizerById(organizerId);
        if (organizer != null) {
            outputArea.setText("Manage Conferences for " + organizer.getName());
            // Add more functionality here like creating and updating conferences
        }
    }

    private void handleManageSpeakers(JTextField organizerIdField, JTextArea outputArea) {
        // Placeholder implementation
        String organizerId = organizerIdField.getText();
        Organizer organizer = organizerService.getOrganizerById(organizerId);
        if (organizer != null) {
            outputArea.setText("Manage Speakers for " + organizer.getName());
            // Add functionality to register speakers and assign to sessions
        }
    }

    private void handleAssignSpeakerToSession(JTextField organizerIdField, JTextArea outputArea) {
        // Placeholder for assigning speakers to sessions
        String organizerId = organizerIdField.getText();
        Organizer organizer = organizerService.getOrganizerById(organizerId);
        if (organizer != null) {
            outputArea.setText("Assign Speaker to Session for " + organizer.getName());
            // Add functionality for selecting a session and assigning a speaker
        }
    }

    public static void main(String[] args) {
        OrganizerRepository repository = new OrganizerRepository("organizers.json");
        OrganizerService service = new OrganizerService(repository);

        new OrganizerGUI(service);
    }
}

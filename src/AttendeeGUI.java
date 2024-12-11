import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class AttendeeGUI {
    private final AttendeeService attendeeService;
    private final Attendee currentAttendee;

    private JFrame frame;
    private JTabbedPane tabbedPane;
    private LoginAndRegistration system;
    private OrganizerService organizerService;
    private ConferenceService conferenceService;
    private SpeakerService speakerService;

    public AttendeeGUI(AttendeeService attendeeService, Attendee currentAttendee,
                       LoginAndRegistration system,
                       OrganizerService organizerService,
                       ConferenceService conferenceService,
                       SpeakerService speakerService) {
        this.attendeeService = attendeeService;
        this.currentAttendee = currentAttendee;
        this.system = system;
        this.organizerService = organizerService;
        this.conferenceService = conferenceService;
        this.speakerService = speakerService;
        initializeGUI();
    }

    private void initializeGUI() {
        frame = new JFrame("Attendee Dashboard - " + currentAttendee.getName());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Create a top panel for logout
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            // Close current frame
            frame.dispose();
            // Show login screen again
            SwingUtilities.invokeLater(() -> new LoginAndRegistrationGUI(system, organizerService, conferenceService, speakerService));
        });
        topPanel.add(logoutButton);

        // Add topPanel and the rest of the tabs
        frame.add(topPanel, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Dashboard", createDashboardPanel());
        tabbedPane.addTab("Register for Conference", createRegisterPanel());
        tabbedPane.addTab("View Registered Conferences", createViewConferencesPanel());
        tabbedPane.addTab("Submit Feedback", createSubmitFeedbackPanel());

        frame.add(tabbedPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }


    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome, " + currentAttendee.getName() + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(welcomeLabel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel instructionLabel = new JLabel("Select a Conference to Register:");
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(instructionLabel, BorderLayout.NORTH);

        JComboBox<Conference> conferenceDropdown = new JComboBox<>(getAvailableConferences().toArray(new Conference[0]));
        conferenceDropdown.setPreferredSize(new Dimension(300, 30));
        JPanel dropdownPanel = new JPanel();
        dropdownPanel.add(conferenceDropdown);
        panel.add(dropdownPanel, BorderLayout.CENTER);

        JButton registerButton = new JButton("Register");
        registerButton.setPreferredSize(new Dimension(150, 30));
        panel.add(registerButton, BorderLayout.SOUTH);

        registerButton.addActionListener(e -> {
            Conference selectedConference = (Conference) conferenceDropdown.getSelectedItem();
            if (selectedConference != null) {
                try {
                    attendeeService.registerForConference(currentAttendee.getId(), selectedConference, currentAttendee.getPassword());
                    JOptionPane.showMessageDialog(frame, "Successfully registered for the conference.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        return panel;
    }

    private JPanel createViewConferencesPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel label = new JLabel("Your Registered Conferences:");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label, BorderLayout.NORTH);

        // Fetch the conferences the attendee is registered for
        List<Conference> registeredConferences = attendeeService.getRegisteredConferences(currentAttendee.getId());

        // Create a combo box to display registered conferences
        JComboBox<Conference> conferenceComboBox = new JComboBox<>(registeredConferences.toArray(new Conference[0]));
        conferenceComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Conference) {
                    Conference conf = (Conference) value;
                    setText(conf.getName());
                }
                return c;
            }
        });

        // Panel for combo box
        JPanel comboPanel = new JPanel();
        comboPanel.add(conferenceComboBox);
        panel.add(comboPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton deregisterButton = new JButton("Deregister");
        JButton viewSessionsButton = new JButton("Check Sessions & Speakers");

        buttonPanel.add(deregisterButton);
        buttonPanel.add(viewSessionsButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Action for deregister
        deregisterButton.addActionListener(e -> {
            Conference selectedConference = (Conference) conferenceComboBox.getSelectedItem();
            if (selectedConference != null) {
                int confirm = JOptionPane.showConfirmDialog(frame,
                        "Are you sure you want to deregister from " + selectedConference.getName() + "?",
                        "Confirm Deregistration", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        attendeeService.deregisterFromConference(currentAttendee.getId(), selectedConference.getId());
                        JOptionPane.showMessageDialog(frame, "Deregistered successfully.");
                        // Update the combo box after deregistration
                        List<Conference> updatedList = attendeeService.getRegisteredConferences(currentAttendee.getId());
                        conferenceComboBox.setModel(new DefaultComboBoxModel<>(updatedList.toArray(new Conference[0])));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(frame, "No conference selected.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Action for viewing sessions & speakers
        viewSessionsButton.addActionListener(e -> {
            Conference selectedConference = (Conference) conferenceComboBox.getSelectedItem();
            if (selectedConference != null) {
                showSessionsAndSpeakersDialog(selectedConference);
            } else {
                JOptionPane.showMessageDialog(frame, "No conference selected.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private void showSessionsAndSpeakersDialog(Conference conference) {
        JDialog dialog = new JDialog(frame, "Sessions & Speakers for " + conference.getName(), true);
        dialog.setSize(400, 300);
        dialog.setLayout(new BorderLayout());

        JTextArea infoArea = new JTextArea();
        infoArea.setEditable(false);

        // Retrieve sessions for the conference
        List<Session> sessions = conference.getSessions();
        if (sessions.isEmpty()) {
            infoArea.append("No sessions available for this conference.\n");
        } else {
            for (Session s : sessions) {
                infoArea.append("Session: " + s.getName() + "\n");
                List<Speaker> speakers = (List<Speaker>) s.getSpeakers();
                if (speakers.isEmpty()) {
                    infoArea.append("  No speakers assigned.\n");
                } else {
                    infoArea.append("  Speakers:\n");
                    for (Speaker sp : speakers) {
                        infoArea.append("    - " + sp.getName() + "\n");
                    }
                }
                infoArea.append("\n");
            }
        }

        dialog.add(new JScrollPane(infoArea), BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(closeButton);
        dialog.add(bottomPanel, BorderLayout.SOUTH);

        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }



    private JPanel createSubmitFeedbackPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel instructionLabel = new JLabel("Submit Feedback for a Conference:");
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(instructionLabel, BorderLayout.NORTH);

        JComboBox<Conference> conferenceDropdown = new JComboBox<>(getAvailableConferences().toArray(new Conference[0]));
        conferenceDropdown.setPreferredSize(new Dimension(300, 30));

        JTextArea feedbackArea = new JTextArea(10, 40);
        feedbackArea.setLineWrap(true);
        feedbackArea.setWrapStyleWord(true);

        JButton submitButton = new JButton("Submit Feedback");
        submitButton.setPreferredSize(new Dimension(150, 30));

        submitButton.addActionListener(e -> {
            Conference selectedConference = (Conference) conferenceDropdown.getSelectedItem();
            String feedback = feedbackArea.getText();

            if (selectedConference != null && !feedback.isEmpty()) {
                try {
                    attendeeService.submitFeedback(currentAttendee.getId(), selectedConference.getId(), feedback);
                    JOptionPane.showMessageDialog(frame, "Feedback submitted successfully.");
                    feedbackArea.setText("");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a conference and enter feedback.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel inputPanel = new JPanel(new BorderLayout());
        JPanel dropdownPanel = new JPanel();
        dropdownPanel.add(conferenceDropdown);
        inputPanel.add(dropdownPanel, BorderLayout.NORTH);
        inputPanel.add(new JScrollPane(feedbackArea), BorderLayout.CENTER);
        inputPanel.add(submitButton, BorderLayout.SOUTH);

        panel.add(inputPanel, BorderLayout.CENTER);
        return panel;
    }

    private List<Conference> getAvailableConferences() {
        List<Conference> allConferences = conferenceService.getConferences();
        List<Conference> registered = attendeeService.getRegisteredConferences(currentAttendee.getId());
        return allConferences.stream()
                .filter(conf -> !registered.contains(conf))
                .collect(Collectors.toList());
    }


    public static void main(String[] args) {
        // Mock data for testing
        AttendeeRepository attendeeRepository = new AttendeeRepository("attendees.json");
        FeedbackRepository feedbackRepository = new FeedbackRepository("feedback.json");
        AttendeeService attendeeService = new AttendeeService(attendeeRepository, feedbackRepository);

        Attendee mockAttendee = new Attendee("A001", "John Doe", "john@example.com", "password123");
        attendeeRepository.save(mockAttendee);
        String organizersFile = "organizers.json";
        OrganizerRepository organizerRepository = new OrganizerRepository(organizersFile);
        OrganizerService organizerService = new OrganizerService(organizerRepository);


        ConferenceRepository conferenceRepository = new ConferenceRepository("conferences.json");
        SessionRepository sessionRepository = new SessionRepository("sessions.json");
        SessionService sessionService = new SessionService(sessionRepository);
        ConferenceService conferenceService = new ConferenceService(sessionService, conferenceRepository);

        // Initialize Speaker repository and Speaker service
        SpeakerRepository speakerRepository = new SpeakerRepository("speakers.json");
        SpeakerService speakerService = new SpeakerService(speakerRepository, sessionRepository);
        LoginAndRegistration loginAndRegistration = new LoginAndRegistration(attendeeRepository,organizerRepository, speakerRepository, feedbackRepository);

        SwingUtilities.invokeLater(() -> new AttendeeGUI(attendeeService, mockAttendee, loginAndRegistration, organizerService, conferenceService, speakerService)
        );
    }
}

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AttendeeGUI {
    private final AttendeeService attendeeService;
    private final Attendee currentAttendee;

    private JFrame frame;
    private JTabbedPane tabbedPane;

    public AttendeeGUI(AttendeeService attendeeService, Attendee currentAttendee) {
        this.attendeeService = attendeeService;
        this.currentAttendee = currentAttendee;
        initializeGUI();
    }

    private void initializeGUI() {
        frame = new JFrame("Attendee Dashboard - " + currentAttendee.getName());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

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

        JTextArea conferencesArea = new JTextArea(15, 40);
        conferencesArea.setEditable(false);

        List<Conference> registeredConferences = attendeeService.getRegisteredConferences(currentAttendee.getId());
        for (Conference conference : registeredConferences) {
            conferencesArea.append(conference.getName() + "\n");
        }

        panel.add(new JScrollPane(conferencesArea), BorderLayout.CENTER);
        return panel;
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
        // This would ideally fetch data from a service or repository
        return currentAttendee.getRegisteredConferences();
    }

    public static void main(String[] args) {
        // Mock data for testing
        AttendeeRepository attendeeRepository = new AttendeeRepository("attendees.json");
        FeedbackRepository feedbackRepository = new FeedbackRepository("feedback.json");
        AttendeeService attendeeService = new AttendeeService(attendeeRepository, feedbackRepository);

        Attendee mockAttendee = new Attendee("A001", "John Doe", "john@example.com", "password123");
        attendeeRepository.save(mockAttendee);

        SwingUtilities.invokeLater(() -> new AttendeeGUI(attendeeService, mockAttendee));
    }
}

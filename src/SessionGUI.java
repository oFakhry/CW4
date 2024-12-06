import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SessionGUI {
    private final SessionService sessionService;

    public SessionGUI(SessionService sessionService) {
        this.sessionService = sessionService;
        initialize();
    }

    private void initialize() {
        JFrame frame = new JFrame("Session Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));

        JLabel nameLabel = new JLabel("Session Name:");
        JTextField nameField = new JTextField();
        JLabel startTimeLabel = new JLabel("Start Time (yyyy-MM-dd HH:mm):");
        JTextField startTimeField = new JTextField();
        JLabel endTimeLabel = new JLabel("End Time (yyyy-MM-dd HH:mm):");
        JTextField endTimeField = new JTextField();
        JLabel statusLabel = new JLabel("Session Status:");
        JComboBox<SessionStatus> statusComboBox = new JComboBox<>(SessionStatus.values());

        JButton createButton = new JButton("Create Session");
        JButton viewButton = new JButton("View All Sessions");

        JTextArea outputArea = new JTextArea(10, 50);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        // Add components to the panel
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(startTimeLabel);
        panel.add(startTimeField);
        panel.add(endTimeLabel);
        panel.add(endTimeField);
        panel.add(statusLabel);
        panel.add(statusComboBox);
        panel.add(createButton);
        panel.add(viewButton);

        frame.add(panel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Action Listeners
        createButton.addActionListener(e -> handleCreateSession(nameField, startTimeField, endTimeField, statusComboBox, outputArea));
        viewButton.addActionListener(e -> handleViewAllSessions(outputArea));

        frame.setVisible(true);
    }

    private void handleCreateSession(JTextField nameField, JTextField startTimeField, JTextField endTimeField, JComboBox<SessionStatus> statusComboBox, JTextArea outputArea) {
        try {
            String name = nameField.getText();
            LocalDateTime startTime = LocalDateTime.parse(startTimeField.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            LocalDateTime endTime = LocalDateTime.parse(endTimeField.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            SessionStatus status = (SessionStatus) statusComboBox.getSelectedItem();

            Session session = sessionService.createSession(name, startTime, endTime, status);
            outputArea.append("Session Created: " + session.getId() + " - " + session.getName() + "\n");

            // Clear input fields
            nameField.setText("");
            startTimeField.setText("");
            endTimeField.setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleViewAllSessions(JTextArea outputArea) {
        outputArea.append("All Sessions:\n");
        for (Session session : sessionService.getAllSessions()) {
            outputArea.append(session.getId() + ": " + session.getName() + " (" + session.getStatus() + ")\n");
        }
    }

    public static void main(String[] args) {
        String filePath = "sessions.json";

        SessionRepository repository = new SessionRepository(filePath);
        SessionService service = new SessionService(repository);

        new SessionGUI(service);
    }
}

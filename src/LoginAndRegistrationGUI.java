import javax.swing.*;
import java.awt.*;


public class LoginAndRegistrationGUI {
    private final LoginAndRegistration system;
    private final OrganizerService organizerService;
    private final ConferenceService conferenceService;
    private final AttendeeService attendeeService;

    public LoginAndRegistrationGUI(LoginAndRegistration system,
                                   OrganizerService organizerService,
                                   ConferenceService conferenceService) {
        this.system = system;
        this.organizerService = organizerService;
        this.conferenceService = conferenceService;
        this.attendeeService = system.getAttendeeService();

        initUI();
    }


    private void initUI() {
        JFrame frame = new JFrame("Conference Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        frame.add(panel);

        JLabel welcomeLabel = new JLabel("Welcome to the Conference Management System!");
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(welcomeLabel);

        JButton registerButton = new JButton("Register");
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.addActionListener(e -> openRegistrationDialog(frame));
        panel.add(registerButton);

        JButton loginButton = new JButton("Login");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.addActionListener(e -> openLoginDialog(frame));
        panel.add(loginButton);

        frame.setVisible(true);
    }

    private void openRegistrationDialog(JFrame parentFrame) {
        JDialog dialog = new JDialog(parentFrame, "Register", true);
        dialog.setSize(300, 300);
        dialog.setLayout(new GridLayout(6, 2));

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();
        dialog.add(nameLabel);
        dialog.add(nameField);

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        dialog.add(emailLabel);
        dialog.add(emailField);

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        dialog.add(passwordLabel);
        dialog.add(passwordField);

        JLabel roleLabel = new JLabel("Role:");
        String[] roles = {"Attendee", "Organizer", "Speaker"};
        JComboBox<String> roleComboBox = new JComboBox<>(roles);
        dialog.add(roleLabel);
        dialog.add(roleComboBox);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String role = (String) roleComboBox.getSelectedItem();

            try {
                system.register(name, email, password, role);
                JOptionPane.showMessageDialog(dialog, "Registration successful!");
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.add(registerButton);
        dialog.add(cancelButton);

        dialog.setVisible(true);
    }

    private void openLoginDialog(JFrame parentFrame) {
        JDialog dialog = new JDialog(parentFrame, "Login", true);
        dialog.setSize(300, 200);
        dialog.setLayout(new GridLayout(5, 2));

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        dialog.add(emailLabel);
        dialog.add(emailField);

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        dialog.add(passwordLabel);
        dialog.add(passwordField);

        JLabel roleLabel = new JLabel("Role:");
        String[] roles = {"Attendee", "Organizer", "Speaker"};
        JComboBox<String> roleComboBox = new JComboBox<>(roles);
        dialog.add(roleLabel);
        dialog.add(roleComboBox);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String role = (String) roleComboBox.getSelectedItem();

            try {
                People user = system.login(email, password, role);
                JOptionPane.showMessageDialog(dialog, "Login successful! Welcome, " + user.getName());
                dialog.dispose();

                if (user instanceof Attendee) {
                    SwingUtilities.invokeLater(() -> new AttendeeGUI(attendeeService, (Attendee) user));
                } else if (user instanceof Organizer) {
                    // Use the shared organizerService and conferenceService from fields
                    SwingUtilities.invokeLater(() -> {
                        OrganizerGUI organizerGUI = new OrganizerGUI(organizerService, conferenceService);
                        organizerGUI.setVisible(true);
                    });
                } else if (user instanceof Speaker) {
                    displayUserInfo(parentFrame, user);
                } else {
                    displayUserInfo(parentFrame, user);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.add(loginButton);
        dialog.add(cancelButton);

        dialog.setVisible(true);
    }



    private void displayUserInfo(JFrame parentFrame, People user) {
        JDialog dialog = new JDialog(parentFrame, "User Information", true);
        dialog.setSize(300, 200);
        dialog.setLayout(new GridLayout(4, 1));

        JLabel nameLabel = new JLabel("Name: " + user.getName());
        JLabel emailLabel = new JLabel("Email: " + user.getEmail());
        JLabel roleLabel = new JLabel("Role: " + user.getClass().getSimpleName());

        dialog.add(nameLabel);
        dialog.add(emailLabel);
        dialog.add(roleLabel);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());
        dialog.add(closeButton);

        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        // Initialize repositories
        AttendeeRepository attendeeRepo = new AttendeeRepository("attendees.json");
        OrganizerRepository organizerRepo = new OrganizerRepository("organizers.json");
        OrganizerService organizerService = new OrganizerService(organizerRepo);
        SpeakerRepository speakerRepo = new SpeakerRepository("speakers.json");
        FeedbackRepository feedbackRepo = new FeedbackRepository("feedback.json");
        SessionRepository sessionRepository = new SessionRepository("sessions.json");
        SessionService sessionService = new SessionService(sessionRepository);
        ConferenceRepository conferenceRepo = new ConferenceRepository("conferences.json");
        ConferenceService ConfService = new ConferenceService(sessionService, conferenceRepo);
        // Initialize LoginAndRegistration system
        LoginAndRegistration system = new LoginAndRegistration(attendeeRepo, organizerRepo, speakerRepo, feedbackRepo);

        // Launch GUI
        SwingUtilities.invokeLater(() -> new LoginAndRegistrationGUI(system, organizerService, ConfService));
    }
}

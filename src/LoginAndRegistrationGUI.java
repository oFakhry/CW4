import javax.swing.*;
import java.awt.*;

public class LoginAndRegistrationGUI {
    private final LoginAndRegistration system;
    private final OrganizerService organizerService;
    private final ConferenceService conferenceService;
    private final AttendeeService attendeeService;
    private final SpeakerService speakerService;
    private JFrame frame;

    private static LoginAndRegistrationGUI instance;

    public LoginAndRegistrationGUI(LoginAndRegistration system,
                                   OrganizerService organizerService,
                                   ConferenceService conferenceService,
                                   SpeakerService speakerService) {
        this.system = system;
        this.organizerService = organizerService;
        this.conferenceService = conferenceService;
        this.speakerService = speakerService;
        this.attendeeService = system.getAttendeeService();

        instance = this; // set the instance for singleton access
        initUI();
    }

    public static LoginAndRegistrationGUI getInstance() {
        return instance;
    }


    public void showLogin() {
        SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }

    private void initUI() {
        frame = new JFrame("Conference Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null); // center the frame

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50)); // padding
        frame.add(panel);

        JLabel welcomeLabel = new JLabel("Welcome to the Conference Management System!");
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 0));
        panel.add(welcomeLabel);

        JButton registerButton = new JButton("Register");
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.setMaximumSize(new Dimension(200, 40));
        registerButton.setFont(new Font("Arial", Font.PLAIN, 16));
        registerButton.addActionListener(e -> openRegistrationDialog(frame));
        panel.add(registerButton);

        panel.add(Box.createRigidArea(new Dimension(0, 20))); // spacing

        JButton loginButton = new JButton("Login");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(200, 40));
        loginButton.setFont(new Font("Arial", Font.PLAIN, 16));
        loginButton.addActionListener(e -> openLoginDialog(frame));
        panel.add(loginButton);

        frame.setVisible(true);
    }

    private void openRegistrationDialog(JFrame parentFrame) {
        JDialog dialog = new JDialog(parentFrame, "Register", true);
        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(parentFrame); // center relative to parent
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(20);
        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(nameLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        dialog.add(nameField, gbc);

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(20);
        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(emailLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        dialog.add(emailField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(passwordLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        dialog.add(passwordField, gbc);

        JLabel roleLabel = new JLabel("Role:");
        String[] roles = {"Attendee", "Organizer", "Speaker"};
        JComboBox<String> roleComboBox = new JComboBox<>(roles);
        gbc.gridx = 0; gbc.gridy = 3;
        dialog.add(roleLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        dialog.add(roleComboBox, gbc);

        JButton registerButton = new JButton("Register");
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        registerButton.setFont(new Font("Arial", Font.PLAIN, 16));
        dialog.add(registerButton, gbc);

        JButton cancelButton = new JButton("Cancel");
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        cancelButton.setFont(new Font("Arial", Font.PLAIN, 16));
        dialog.add(cancelButton, gbc);

        registerButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String role = (String) roleComboBox.getSelectedItem();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || role == null) {
                JOptionPane.showMessageDialog(dialog, "Please fill out all fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                system.register(name, email, password, role);
                JOptionPane.showMessageDialog(dialog, "Registration successful!");
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private void openLoginDialog(JFrame parentFrame) {
        JDialog dialog = new JDialog(parentFrame, "Login", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(parentFrame); // center relative to parent
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(20);
        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(emailLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        dialog.add(emailField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(passwordLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        dialog.add(passwordField, gbc);

        JLabel roleLabel = new JLabel("Role:");
        String[] roles = {"Attendee", "Organizer", "Speaker"};
        JComboBox<String> roleComboBox = new JComboBox<>(roles);
        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(roleLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        dialog.add(roleComboBox, gbc);

        JButton loginButton = new JButton("Login");
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        loginButton.setFont(new Font("Arial", Font.PLAIN, 16));
        dialog.add(loginButton, gbc);

        JButton cancelButton = new JButton("Cancel");
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        cancelButton.setFont(new Font("Arial", Font.PLAIN, 16));
        dialog.add(cancelButton, gbc);

        loginButton.addActionListener(e -> {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String role = (String) roleComboBox.getSelectedItem();

            if (email.isEmpty() || password.isEmpty() || role == null) {
                JOptionPane.showMessageDialog(dialog, "Please fill out all fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                People user = system.login(email, password, role);
                JOptionPane.showMessageDialog(dialog, "Login successful! Welcome, " + user.getName());
                dialog.dispose();
                frame.setVisible(false); // hide login window

                if (user instanceof Attendee) {
                    SwingUtilities.invokeLater(() -> new AttendeeGUI(attendeeService, (Attendee) user, system, organizerService, conferenceService, speakerService));
                } else if (user instanceof Organizer) {
                    SwingUtilities.invokeLater(() -> {
                        OrganizerGUI organizerGUI = new OrganizerGUI(organizerService, conferenceService, speakerService, system);
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

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private void displayUserInfo(JFrame parentFrame, People user) {
        JDialog dialog = new JDialog(parentFrame, "User Information", true);
        dialog.setSize(350, 200);
        dialog.setLocationRelativeTo(parentFrame); // center relative to parent
        dialog.setLayout(new GridLayout(4, 1));

        JLabel nameLabel = new JLabel("Name: " + user.getName());
        JLabel emailLabel = new JLabel("Email: " + user.getEmail());
        JLabel roleLabel = new JLabel("Role: " + user.getClass().getSimpleName());

        dialog.add(nameLabel);
        dialog.add(emailLabel);
        dialog.add(roleLabel);

        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Arial", Font.PLAIN, 16));
        closeButton.addActionListener(e -> dialog.dispose());
        dialog.add(closeButton);

        dialog.setVisible(true);
    }

    public static void showLoginGUI(LoginAndRegistration system,
                                    OrganizerService organizerService,
                                    ConferenceService conferenceService,
                                    SpeakerService speakerService) {
        SwingUtilities.invokeLater(() -> new LoginAndRegistrationGUI(system, organizerService, conferenceService, speakerService));
    }

    public static void main(String[] args) {
        // Initialize repositories and services
        String organizersFile = "organizers.json";
        OrganizerRepository organizerRepository = new OrganizerRepository(organizersFile);
        OrganizerService organizerService = new OrganizerService(organizerRepository);

        FeedbackRepository feedbackRepository = new FeedbackRepository("feedback.json");

        AttendeeRepository attendeeRepository = new AttendeeRepository("attendees.json");
        AttendeeService attendeeService = new AttendeeService(attendeeRepository, feedbackRepository);

        ConferenceRepository conferenceRepository = new ConferenceRepository("conferences.json");
        SessionRepository sessionRepository = new SessionRepository("sessions.json");
        SessionService sessionService = new SessionService(sessionRepository);
        ConferenceService conferenceService = new ConferenceService(sessionService, conferenceRepository);

        SpeakerRepository speakerRepository = new SpeakerRepository("speakers.json");
        SpeakerService speakerService = new SpeakerService(speakerRepository, sessionRepository);

        LoginAndRegistration system = new LoginAndRegistration(attendeeRepository, organizerRepository, speakerRepository, feedbackRepository);

        // Launch GUI
        SwingUtilities.invokeLater(() -> new LoginAndRegistrationGUI(system, organizerService, conferenceService, speakerService));
    }
}

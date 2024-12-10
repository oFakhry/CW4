import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginAndRegistrationGUI {
    private final LoginAndRegistration system;

    public LoginAndRegistrationGUI(LoginAndRegistration system) {
        this.system = system;
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

    public static void main(String[] args) {
        // Initialize repositories
        AttendeeRepository attendeeRepo = new AttendeeRepository("attendees.json");
        OrganizerRepository organizerRepo = new OrganizerRepository("organizers.json");
        SpeakerRepository speakerRepo = new SpeakerRepository("speakers.json");

        // Initialize LoginAndRegistration system
        LoginAndRegistration system = new LoginAndRegistration(attendeeRepo, organizerRepo, speakerRepo);

        // Launch GUI
        SwingUtilities.invokeLater(() -> new LoginAndRegistrationGUI(system));
    }
}

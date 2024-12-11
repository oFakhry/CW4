import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class OrganizerGUI extends JFrame {

    private OrganizerService organizerService;
    private ConferenceService conferenceService;

    // Components for the "Organizer Management" tab
    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton saveOrganizerButton;
    private JButton clearOrganizerFormButton;

    // Components for the "View Organizers" tab
    private JTable organizerTable;
    private DefaultTableModel organizerTableModel;
    private JButton deleteOrganizerButton;
    private JButton editSelectedOrganizerButton;

    // Components for the "Conferences" tab
    private JTable conferenceTable;
    private DefaultTableModel conferenceTableModel;
    private JTextField conferenceNameField;
    private JTextField conferenceDescField;
    private JTextField startDateField; // format: YYYY-MM-DD
    private JTextField endDateField;   // format: YYYY-MM-DD
    private JButton addConferenceButton;
    private JButton updateConferenceButton;
    private JButton deleteConferenceButton;
    private JComboBox<String> organizerComboBox;

    // Session Management UI in Conferences tab
    private JTextField sessionNameField;
    private JTextField sessionStartField; // format: yyyy-MM-dd HH:mm
    private JTextField sessionEndField;
    private JComboBox<SessionStatus> sessionStatusCombo;
    private JButton createSessionButton;
    private JButton refreshSessionsButton;
    private JTable sessionTable;
    private DefaultTableModel sessionTableModel;

    // Assign Speaker to Session
    private JComboBox<Speaker> speakerComboBox;
    private JButton assignSpeakerButton;

    // Components for the "Speakers" tab
    private JTable speakerTable;
    private DefaultTableModel speakerTableModel;
    private JTextField speakerNameField;
    private JTextField speakerEmailField;
    private JTextField speakerBioField;
    private JPasswordField speakerPasswordField;
    private JButton addSpeakerButton;
    private JButton deleteSpeakerButton;

    // Currently selected organizer
    private Organizer selectedOrganizer;
    // Currently selected conference
    private Conference selectedConference;
    // Currently selected speaker
    private Speaker selectedSpeaker;

    public OrganizerGUI(OrganizerService organizerService, ConferenceService conferenceService) {
        this.organizerService = organizerService;
        this.conferenceService = conferenceService;

        setTitle("Organizer Management");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create tabs
        JTabbedPane tabbedPane = new JTabbedPane();

        // 1. Organizer Management Tab (Create new only)
        tabbedPane.addTab("Organizer Management", createOrganizerManagementPanel());

        // 2. View Organizers Tab
        tabbedPane.addTab("View Organizers", createViewOrganizersPanel());

        // 3. Conferences Tab
        JPanel conferencesPanel = createConferencesPanel(tabbedPane);
        tabbedPane.addTab("Conferences", conferencesPanel);

        // 4. Speakers Tab
        JPanel speakersPanel = createSpeakersPanel();
        tabbedPane.addTab("Speakers", speakersPanel);

        // Add a listener to handle tab changes
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int selectedIndex = tabbedPane.getSelectedIndex();
                String selectedTitle = tabbedPane.getTitleAt(selectedIndex);
                if ("Conferences".equals(selectedTitle)) {
                    refreshOrganizerComboBox();
                    loadConferenceTableData();
                    loadSessionTableData(); // Refresh sessions for currently selected conference
                    populateSpeakerComboBox(); // Refresh the speaker dropdown
                } else if ("Speakers".equals(selectedTitle)) {
                    loadSpeakerTableData();
                }
            }
        });

        add(tabbedPane, BorderLayout.CENTER);

        // Load the organizer table data initially
        loadOrganizerTableData();
    }

    private void loadSessionTableData() {
        if (sessionTableModel == null) return;
        sessionTableModel.setRowCount(0);
        if (selectedConference == null) {
            return; // No conference selected, no sessions
        }

        // Retrieve sessions from the selectedConference
        List<Session> sessions = selectedConference.getSessions();
        for (Session s : sessions) {
            sessionTableModel.addRow(new Object[]{s.getId(), s.getName(), s.getStatus()});
        }
    }

    private JPanel createOrganizerManagementPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 5, 5, 5);

        // Name Field
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(new JLabel("Name:"), gbc);

        gbc.gridx = 1; gbc.anchor = GridBagConstraints.LINE_START;
        nameField = new JTextField(20);
        panel.add(nameField, gbc);

        // Email Field
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1; gbc.anchor = GridBagConstraints.LINE_START;
        emailField = new JTextField(20);
        panel.add(emailField, gbc);

        // Password Field
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1; gbc.anchor = GridBagConstraints.LINE_START;
        passwordField = new JPasswordField(20);
        panel.add(passwordField, gbc);

        // Buttons
        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.LINE_END;
        saveOrganizerButton = new JButton("Create Organizer");
        panel.add(saveOrganizerButton, gbc);

        gbc.gridx = 1; gbc.anchor = GridBagConstraints.LINE_START;
        clearOrganizerFormButton = new JButton("Clear Form");
        panel.add(clearOrganizerFormButton, gbc);

        // Button actions
        saveOrganizerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNewOrganizer();
            }
        });

        clearOrganizerFormButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearOrganizerForm();
            }
        });

        return panel;
    }

    private JPanel createViewOrganizersPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        organizerTableModel = new DefaultTableModel(new Object[] {"ID", "Name", "Email"}, 0);
        organizerTable = new JTable(organizerTableModel);
        organizerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(organizerTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        editSelectedOrganizerButton = new JButton("Edit Selected");
        deleteOrganizerButton = new JButton("Delete Selected");

        buttonPanel.add(editSelectedOrganizerButton);
        buttonPanel.add(deleteOrganizerButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Button actions
        editSelectedOrganizerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editSelectedOrganizerPopup();
            }
        });

        deleteOrganizerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedOrganizer();
            }
        });

        return panel;
    }

    private void editSelectedOrganizerPopup() {
        int selectedRow = organizerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an organizer from the table.");
            return;
        }

        String id = (String) organizerTableModel.getValueAt(selectedRow, 0);
        Organizer organizer = organizerService.getOrganizerById(id);

        if (organizer == null) {
            JOptionPane.showMessageDialog(this, "Organizer not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create a popup dialog for editing this organizer
        JDialog dialog = new JDialog(this, "Edit Organizer", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4,4,4,4);
        gbc.anchor = GridBagConstraints.LINE_END;

        JLabel nameLabel = new JLabel("Name:");
        JLabel emailLabel = new JLabel("Email:");
        JLabel passwordLabel = new JLabel("Password:");

        JTextField editNameField = new JTextField(20);
        editNameField.setText(organizer.getName());

        JTextField editEmailField = new JTextField(20);
        editEmailField.setText(organizer.getEmail());

        JPasswordField editPasswordField = new JPasswordField(20);
        editPasswordField.setText(organizer.getPassword());

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.LINE_END;
        dialog.add(nameLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.LINE_START;
        dialog.add(editNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.LINE_END;
        dialog.add(emailLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.LINE_START;
        dialog.add(editEmailField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.LINE_END;
        dialog.add(passwordLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.LINE_START;
        dialog.add(editPasswordField, gbc);

        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        dialog.add(buttonPanel, gbc);

        saveButton.addActionListener(ev -> {
            String newName = editNameField.getText().trim();
            String newEmail = editEmailField.getText().trim();
            String newPassword = new String(editPasswordField.getPassword()).trim();

            if (newName.isEmpty() || newEmail.isEmpty() || newPassword.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill out all fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            organizer.setName(newName);
            organizer.setEmail(newEmail);
            organizer.setPassword(newPassword);

            organizerService.createOrUpdateOrganizer(organizer, newPassword);
            JOptionPane.showMessageDialog(dialog, "Organizer updated successfully!");
            loadOrganizerTableData();
            dialog.dispose();
        });

        cancelButton.addActionListener(ev -> dialog.dispose());

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private JPanel createConferencesPanel(JTabbedPane tabbedPane) {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Top panel with organizer selection
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Select Organizer:"));

        organizerComboBox = new JComboBox<>();
        topPanel.add(organizerComboBox);

        organizerComboBox.addActionListener(e -> {
            String selectedItem = (String) organizerComboBox.getSelectedItem();
            if (selectedItem != null && !selectedItem.isEmpty()) {
                String selectedId = selectedItem.split(" - ")[0];
                Organizer org = organizerService.getOrganizerById(selectedId);
                if (org != null) {
                    selectedOrganizer = org;
                    loadConferenceTableData();
                    loadSessionTableData();
                    populateSpeakerComboBox();
                }
            }
        });

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // --------------------- CONFERENCE AREA ---------------------

        // Left: Conference Table
        conferenceTableModel = new DefaultTableModel(new Object[]{"ID", "Name"}, 0);
        conferenceTable = new JTable(conferenceTableModel);
        conferenceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane conferenceScroll = new JScrollPane(conferenceTable);

        // Right: Conference Form and Buttons
        JPanel conferenceFormPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4,4,4,4);
        gbc.anchor = GridBagConstraints.LINE_END;

        gbc.gridx = 0; gbc.gridy = 0;
        conferenceFormPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.LINE_START;
        conferenceNameField = new JTextField(20);
        conferenceFormPanel.add(conferenceNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.LINE_END;
        conferenceFormPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.LINE_START;
        conferenceDescField = new JTextField(20);
        conferenceFormPanel.add(conferenceDescField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.LINE_END;
        conferenceFormPanel.add(new JLabel("Start Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.LINE_START;
        startDateField = new JTextField(10);
        conferenceFormPanel.add(startDateField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.LINE_END;
        conferenceFormPanel.add(new JLabel("End Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.LINE_START;
        endDateField = new JTextField(10);
        conferenceFormPanel.add(endDateField, gbc);

        JPanel conferenceButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addConferenceButton = new JButton("Add Conference");
        updateConferenceButton = new JButton("Update Conference");
        deleteConferenceButton = new JButton("Delete Conference");
        conferenceButtonPanel.add(addConferenceButton);
        conferenceButtonPanel.add(updateConferenceButton);
        conferenceButtonPanel.add(deleteConferenceButton);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        conferenceFormPanel.add(conferenceButtonPanel, gbc);

        // Conference actions
        addConferenceButton.addActionListener(e -> addConference());
        updateConferenceButton.addActionListener(e -> updateConference());
        deleteConferenceButton.addActionListener(e -> deleteConference());

        conferenceTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && conferenceTable.getSelectedRow() != -1) {
                String confId = (String) conferenceTableModel.getValueAt(conferenceTable.getSelectedRow(), 0);
                Conference globalConf = conferenceService.getConferences().stream()
                        .filter(c -> c.getId().equals(confId))
                        .findFirst()
                        .orElse(null);
                selectedConference = globalConf;

                if (selectedConference != null) {
                    conferenceNameField.setText(selectedConference.getName());
                    conferenceDescField.setText(selectedConference.getDescription());
                    startDateField.setText(selectedConference.getStartDate().toString());
                    endDateField.setText(selectedConference.getEndDate().toString());
                    loadSessionTableData();
                    populateSpeakerComboBox();
                }
            }
        });

        // SplitPane for Conferences (Horizontal)
        JSplitPane conferenceSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, conferenceScroll, conferenceFormPanel);
        conferenceSplit.setDividerLocation(300);

        // --------------------- SESSION AREA ---------------------

        // Left: Session Table
        sessionTableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Status"}, 0);
        sessionTable = new JTable(sessionTableModel);
        sessionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane sessionScroll = new JScrollPane(sessionTable);

        // Right: Session Form and Buttons
        JPanel sessionFormPanel = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(4,4,4,4);
        gbc.anchor = GridBagConstraints.LINE_END;

        JLabel sessionNameLabel = new JLabel("Session Name:");
        JLabel sessionStartLabel = new JLabel("Start Time (yyyy-MM-dd HH:mm):");
        JLabel sessionEndLabel = new JLabel("End Time (yyyy-MM-dd HH:mm):");
        JLabel sessionStatusLabel = new JLabel("Session Status:");

        sessionNameField = new JTextField(20);
        sessionStartField = new JTextField(20);
        sessionEndField = new JTextField(20);
        sessionStatusCombo = new JComboBox<>(SessionStatus.values());

        gbc.gridx = 0; gbc.gridy = 0; sessionFormPanel.add(sessionNameLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.LINE_START; sessionFormPanel.add(sessionNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.LINE_END; sessionFormPanel.add(sessionStartLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.LINE_START; sessionFormPanel.add(sessionStartField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.LINE_END; sessionFormPanel.add(sessionEndLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.LINE_START; sessionFormPanel.add(sessionEndField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.LINE_END; sessionFormPanel.add(sessionStatusLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.LINE_START; sessionFormPanel.add(sessionStatusCombo, gbc);

        JPanel sessionButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        createSessionButton = new JButton("Create Session");
        refreshSessionsButton = new JButton("Refresh Sessions");
        sessionButtonPanel.add(createSessionButton);
        sessionButtonPanel.add(refreshSessionsButton);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        sessionFormPanel.add(sessionButtonPanel, gbc);

        createSessionButton.addActionListener(e -> {
            createSessionForConference();
            populateSpeakerComboBox();
        });
        refreshSessionsButton.addActionListener(e -> {
            loadSessionTableData();
            populateSpeakerComboBox();
        });

        // ----- ASSIGN SPEAKER TO SESSION -----
        JPanel assignSpeakerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints asGbc = new GridBagConstraints();
        asGbc.insets = new Insets(4,4,4,4);
        asGbc.anchor = GridBagConstraints.LINE_END;

        JLabel speakerAssignLabel = new JLabel("Assign Speaker:");
        asGbc.gridx = 0; asGbc.gridy = 0; assignSpeakerPanel.add(speakerAssignLabel, asGbc);

        speakerComboBox = new JComboBox<>();
        asGbc.gridx = 1; asGbc.anchor = GridBagConstraints.LINE_START; assignSpeakerPanel.add(speakerComboBox, asGbc);

        assignSpeakerButton = new JButton("Assign Speaker to Session");
        asGbc.gridx = 0; asGbc.gridy = 1; asGbc.gridwidth = 2; asGbc.anchor = GridBagConstraints.CENTER;
        assignSpeakerPanel.add(assignSpeakerButton, asGbc);

        assignSpeakerButton.addActionListener(e -> assignSpeakerToSession());

        // Combine sessionFormPanel and assignSpeakerPanel vertically
        JPanel sessionRightPanel = new JPanel(new BorderLayout());
        sessionRightPanel.add(sessionFormPanel, BorderLayout.NORTH);
        sessionRightPanel.add(assignSpeakerPanel, BorderLayout.SOUTH);

        // Session Split (Horizontal)
        JSplitPane sessionSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sessionScroll, sessionRightPanel);
        sessionSplit.setDividerLocation(300);

        // Now stack the conferenceSplit and sessionSplit vertically
        JSplitPane mainSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, conferenceSplit, sessionSplit);
        mainSplit.setDividerLocation(250);

        mainPanel.add(mainSplit, BorderLayout.CENTER);

        return mainPanel;
    }

    private JPanel createSpeakersPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        speakerTableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Email"}, 0);
        speakerTable = new JTable(speakerTableModel);
        speakerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane speakerScroll = new JScrollPane(speakerTable);
        panel.add(speakerScroll, BorderLayout.CENTER);

        // Form to add a new speaker
        JPanel speakerFormPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4,4,4,4);

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.LINE_END;
        speakerFormPanel.add(new JLabel("Name:"), gbc);

        gbc.gridx = 1; gbc.anchor = GridBagConstraints.LINE_START;
        speakerNameField = new JTextField(20);
        speakerFormPanel.add(speakerNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.LINE_END;
        speakerFormPanel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1; gbc.anchor = GridBagConstraints.LINE_START;
        speakerEmailField = new JTextField(20);
        speakerFormPanel.add(speakerEmailField, gbc);

        // Add bio field
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.LINE_END;
        speakerFormPanel.add(new JLabel("Bio:"), gbc);

        gbc.gridx = 1; gbc.anchor = GridBagConstraints.LINE_START;
        speakerBioField = new JTextField(20);
        speakerFormPanel.add(speakerBioField, gbc);

        // Add password field
        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.LINE_END;
        speakerFormPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1; gbc.anchor = GridBagConstraints.LINE_START;
        speakerPasswordField = new JPasswordField(20);
        speakerFormPanel.add(speakerPasswordField, gbc);

        JPanel speakerButtonPanel = new JPanel();
        addSpeakerButton = new JButton("Add Speaker");
        deleteSpeakerButton = new JButton("Delete Speaker");
        speakerButtonPanel.add(addSpeakerButton);
        speakerButtonPanel.add(deleteSpeakerButton);

        addSpeakerButton.addActionListener(e -> addSpeaker());
        deleteSpeakerButton.addActionListener(e -> deleteSpeaker());

        speakerTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && speakerTable.getSelectedRow() != -1 && selectedOrganizer != null) {
                String speakerId = (String) speakerTableModel.getValueAt(speakerTable.getSelectedRow(), 0);
                // Find speaker in selectedOrganizer's speakers
                selectedSpeaker = selectedOrganizer.getRegisteredSpeakers().stream()
                        .filter(s -> s.getId().equals(speakerId))
                        .findFirst()
                        .orElse(null);
            }
        });

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(speakerFormPanel, BorderLayout.CENTER);
        bottomPanel.add(speakerButtonPanel, BorderLayout.SOUTH);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void createNewOrganizer() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill out all fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Creating a new organizer with a fresh unique ID
        String generatedId = String.valueOf(IDGenerator.generateOrganizerId());
        Organizer newOrganizer = new Organizer(generatedId, name, email, password);
        organizerService.createOrUpdateOrganizer(newOrganizer, password);
        JOptionPane.showMessageDialog(this, "Organizer created successfully!");
        clearOrganizerForm();
        loadOrganizerTableData();
    }

    private void clearOrganizerForm() {
        nameField.setText("");
        emailField.setText("");
        passwordField.setText("");
    }

    private void loadOrganizerTableData() {
        organizerTableModel.setRowCount(0);
        List<Organizer> organizers = organizerService.getAllOrganizers();
        for (Organizer org : organizers) {
            organizerTableModel.addRow(new Object[] {org.getId(), org.getName(), org.getEmail()});
        }
    }

    private void refreshOrganizerComboBox() {
        organizerComboBox.removeAllItems();
        List<Organizer> allOrganizers = organizerService.getAllOrganizers();
        for (Organizer org : allOrganizers) {
            organizerComboBox.addItem(org.getId() + " - " + org.getName());
        }
        // Optionally select the first organizer automatically, if available
        if (!allOrganizers.isEmpty()) {
            organizerComboBox.setSelectedIndex(0);
            selectedOrganizer = allOrganizers.get(0);
        } else {
            selectedOrganizer = null;
        }
    }

    private void deleteSelectedOrganizer() {
        int selectedRow = organizerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an organizer to delete.");
            return;
        }

        String id = (String) organizerTableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete organizer with ID: " + id + "?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            organizerService.deleteOrganizer(id);
            loadOrganizerTableData();
            JOptionPane.showMessageDialog(this, "Organizer deleted successfully.");
            selectedOrganizer = null; // Clear current selection
        }
    }

    private void loadConferenceTableData() {
        if (conferenceTableModel == null) return;
        conferenceTableModel.setRowCount(0);

        // Load all conferences globally
        List<Conference> allConferences = conferenceService.getConferences();
        for (Conference conf : allConferences) {
            conferenceTableModel.addRow(new Object[]{conf.getId(), conf.getName()});
        }
    }

    private void addConference() {
        if (selectedOrganizer == null) {
            JOptionPane.showMessageDialog(this, "Please select an organizer first from the dropdown.");
            return;
        }

        String name = conferenceNameField.getText().trim();
        String description = conferenceDescField.getText().trim();
        String startDateStr = startDateField.getText().trim();
        String endDateStr = endDateField.getText().trim();

        if (name.isEmpty() || description.isEmpty() || startDateStr.isEmpty() || endDateStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill out all conference fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            LocalDate startDate = LocalDate.parse(startDateStr);
            LocalDate endDate = LocalDate.parse(endDateStr);

            String conferenceId = String.valueOf(IDGenerator.generateConferenceId());
            // Create the conference using the service (saved globally)
            conferenceService.createConference(conferenceId, name, description, startDate, endDate);

            // Add conference to organizer as well
            Conference newConference = conferenceService.getConferences().stream()
                    .filter(c -> c.getId().equals(conferenceId))
                    .findFirst()
                    .orElse(null);

            if (newConference != null) {
                selectedOrganizer.setUpConference(newConference);
                organizerService.createOrUpdateOrganizer(selectedOrganizer, selectedOrganizer.getPassword());
                JOptionPane.showMessageDialog(this, "Conference created successfully!");
                loadConferenceTableData();
                clearConferenceForm();
            } else {
                JOptionPane.showMessageDialog(this, "Error: Conference not found after creation.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid date format or other error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateConference() {
        if (selectedConference == null) {
            JOptionPane.showMessageDialog(this, "Please select a conference to update.");
            return;
        }

        String name = conferenceNameField.getText().trim();
        String description = conferenceDescField.getText().trim();
        String startDateStr = startDateField.getText().trim();
        String endDateStr = endDateField.getText().trim();

        if (name.isEmpty() || description.isEmpty() || startDateStr.isEmpty() || endDateStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill out all conference fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            LocalDate startDate = LocalDate.parse(startDateStr);
            LocalDate endDate = LocalDate.parse(endDateStr);

            // Update the conference globally
            Conference conf = conferenceService.getConferences().stream()
                    .filter(c -> c.getId().equals(selectedConference.getId()))
                    .findFirst()
                    .orElse(null);

            if (conf != null) {
                conf.setName(name);
                conf.setDescription(description);
                conf.setStartDate(startDate);
                conf.setEndDate(endDate);
                conferenceService.saveConferencesToFile(conferenceService.getConferences());

                if (selectedOrganizer != null) {
                    organizerService.createOrUpdateOrganizer(selectedOrganizer, selectedOrganizer.getPassword());
                }
                JOptionPane.showMessageDialog(this, "Conference updated successfully!");
                loadConferenceTableData();
            } else {
                JOptionPane.showMessageDialog(this, "Conference not found for update.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid date format or other error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteConference() {
        if (selectedConference == null) {
            JOptionPane.showMessageDialog(this, "Please select a conference to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete conference: " + selectedConference.getName() + "?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // Remove from global list
            List<Conference> allConferences = conferenceService.getConferences().stream()
                    .filter(c -> !c.getId().equals(selectedConference.getId()))
                    .collect(Collectors.toList());

            conferenceService.saveConferencesToFile(allConferences);

            // Remove from any organizer who had it
            List<Organizer> allOrganizers = organizerService.getAllOrganizers();
            for (Organizer org : allOrganizers) {
                org.getConferences().removeIf(c -> c.getId().equals(selectedConference.getId()));
                organizerService.createOrUpdateOrganizer(org, org.getPassword());
            }

            JOptionPane.showMessageDialog(this, "Conference deleted successfully!");
            loadConferenceTableData();
            clearConferenceForm();
        }
    }

    private void clearConferenceForm() {
        selectedConference = null;
        conferenceNameField.setText("");
        conferenceDescField.setText("");
        startDateField.setText("");
        endDateField.setText("");
    }

    private void createSessionForConference() {
        if (selectedConference == null) {
            JOptionPane.showMessageDialog(this, "Please select a conference first.");
            return;
        }

        String sessionName = sessionNameField.getText().trim();
        String startTimeStr = sessionStartField.getText().trim();
        String endTimeStr = sessionEndField.getText().trim();
        SessionStatus status = (SessionStatus) sessionStatusCombo.getSelectedItem();

        if (sessionName.isEmpty() || startTimeStr.isEmpty() || endTimeStr.isEmpty() || status == null) {
            JOptionPane.showMessageDialog(this, "Please fill out all session fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            LocalDateTime startTime = LocalDateTime.parse(startTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            LocalDateTime endTime = LocalDateTime.parse(endTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            // Generate a unique session ID
            String sessionId = String.valueOf(IDGenerator.generateSessionId());

            // Create a new Session instance directly
            Session newSession = new Session(sessionId, sessionName, startTime, endTime, status);

            // Get the existing sessions and append the new one
            List<Session> allSessions = selectedConference.getSessionService().getAllSessions();
            allSessions.add(newSession);

            // Save the updated session list
            selectedConference.getSessionService().saveSessionsToFile(allSessions);

            // Also save conference and organizer if needed
            conferenceService.saveConferencesToFile(conferenceService.getConferences());

            JOptionPane.showMessageDialog(this, "Session created and assigned to conference!");
            loadSessionTableData();

            // Clear session fields
            sessionNameField.setText("");
            sessionStartField.setText("");
            sessionEndField.setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error creating session: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void assignSpeakerToSession() {
        // Get selected session
        int selectedSessionRow = sessionTable.getSelectedRow();
        if (selectedSessionRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a session first.");
            return;
        }

        String selectedSessionId = (String) sessionTableModel.getValueAt(selectedSessionRow, 0);
        Session selectedSession = selectedConference.getSessions().stream()
                .filter(s -> s.getId().equals(selectedSessionId))
                .findFirst()
                .orElse(null);

        if (selectedSession == null) {
            JOptionPane.showMessageDialog(this, "Selected session not found.");
            return;
        }

        // Get selected speaker
        Speaker sp = (Speaker) speakerComboBox.getSelectedItem();
        if (sp == null) {
            JOptionPane.showMessageDialog(this, "Please select a speaker to assign.");
            return;
        }

        // Assign speaker to session and session to speaker
        selectedSession.addSpeaker(sp);
        sp.addSession(selectedSession); // assuming Speaker has addSession method

        // Save updated sessions
        List<Session> allSessions = selectedConference.getSessionService().getAllSessions();
        // Update the existing session in the list with the modified one
        allSessions.removeIf(s -> s.getId().equals(selectedSession.getId()));
        allSessions.add(selectedSession);
        selectedConference.getSessionService().saveSessionsToFile(allSessions);

        // Update organizer as speakers belong to organizer
        organizerService.createOrUpdateOrganizer(selectedOrganizer, selectedOrganizer.getPassword());

        // Save updated conferences if needed
        conferenceService.saveConferencesToFile(conferenceService.getConferences());

        JOptionPane.showMessageDialog(this, "Speaker assigned to session successfully!");
    }

    private void populateSpeakerComboBox() {
        if (speakerComboBox == null) return;
        speakerComboBox.removeAllItems();
        if (selectedOrganizer == null) return;
        Set<Speaker> speakers = selectedOrganizer.getRegisteredSpeakers();
        for (Speaker sp : speakers) {
            speakerComboBox.addItem(sp);
        }
    }

    private void addSpeaker() {
        if (selectedOrganizer == null) {
            JOptionPane.showMessageDialog(this, "Please select an organizer first.");
            return;
        }

        String name = speakerNameField.getText().trim();
        String email = speakerEmailField.getText().trim();
        String bio = speakerBioField.getText().trim();
        String password = new String(speakerPasswordField.getPassword()).trim();

        if (name.isEmpty() || email.isEmpty() || bio.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill out all fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String speakerId = String.valueOf(IDGenerator.generateSpeakerId());
        Speaker newSpeaker = new Speaker(speakerId, name, email, bio, password);
        selectedOrganizer.registerSpeaker(newSpeaker);
        organizerService.createOrUpdateOrganizer(selectedOrganizer, selectedOrganizer.getPassword());

        JOptionPane.showMessageDialog(this, "Speaker added successfully!");
        loadSpeakerTableData();
        clearSpeakerForm();
    }

    private void loadSpeakerTableData() {
        if (speakerTableModel == null) return;
        speakerTableModel.setRowCount(0);
        if (selectedOrganizer == null) {
            // No organizer selected, no speakers
            return;
        }

        Set<Speaker> speakers = selectedOrganizer.getRegisteredSpeakers();
        for (Speaker spk : speakers) {
            speakerTableModel.addRow(new Object[] { spk.getId(), spk.getName(), spk.getEmail() });
        }
    }

    private void deleteSpeaker() {
        if (selectedOrganizer == null || selectedSpeaker == null) {
            JOptionPane.showMessageDialog(this, "Please select a speaker to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete speaker: " + selectedSpeaker.getName() + "?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            selectedOrganizer.getRegisteredSpeakers().removeIf(sp -> sp.getId().equals(selectedSpeaker.getId()));
            organizerService.createOrUpdateOrganizer(selectedOrganizer, selectedOrganizer.getPassword());
            JOptionPane.showMessageDialog(this, "Speaker deleted successfully!");
            loadSpeakerTableData();
            clearSpeakerForm();
        }
    }

    private void clearSpeakerForm() {
        speakerNameField.setText("");
        speakerEmailField.setText("");
        speakerBioField.setText("");
        speakerPasswordField.setText("");
        selectedSpeaker = null;
    }

    public static void main(String[] args) {
        // Initialize repositories and services
        String organizersFile = "organizers.json";
        OrganizerRepository organizerRepository = new OrganizerRepository(organizersFile);
        OrganizerService organizerService = new OrganizerService(organizerRepository);

        ConferenceRepository conferenceRepository = new ConferenceRepository("conferences.json");
        SessionRepository sessionRepository = new SessionRepository("sessions.json");
        SessionService sessionService = new SessionService(sessionRepository);
        ConferenceService conferenceService = new ConferenceService(sessionService, conferenceRepository);

        SwingUtilities.invokeLater(() -> {
            OrganizerGUI ui = new OrganizerGUI(organizerService, conferenceService);
            ui.setVisible(true);
        });
    }
}

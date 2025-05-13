package org.example.View;

import org.example.Connection.ConnectionFactory;
import org.example.Model.User;
import org.example.Repository.UserRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

@SuppressWarnings("ALL")
public class UserListView extends JPanel {

    private DefaultTableModel tableModel;
    private JTable userTable;
    private final UserRepository userRepository;

    public UserListView() {
        userRepository = new UserRepository(ConnectionFactory.getConnection());
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Create a table model
        tableModel = new DefaultTableModel();

        // Get all users from the UserRepository and populate the table
        refreshUsersTable();

        // Create the JTable with the table model
        userTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };

        // Set the preferred size of the table
        userTable.setPreferredScrollableViewportSize(new Dimension(600, 200));

        JScrollPane scrollPane = new JScrollPane(userTable);

        // Add the scroll pane to the panel
        add(scrollPane, BorderLayout.CENTER);

        // Add buttons at the bottom
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS)); // Horizontal layout

        // Create the "Edit" button
        JButton editButton = new JButton("EDITEAZĂ");
        editButton.addActionListener(e -> editUser());

        // Create the "Delete" button
        JButton deleteButton = new JButton("ȘTERGE");
        deleteButton.addActionListener(e -> deleteSelectedUser());

        // Create the "Refresh" button
        JButton refreshButton = new JButton("REFRESH");
        refreshButton.addActionListener(e -> refreshUsersTable());

        // Add buttons to the button panel
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(Box.createHorizontalGlue()); // Add spacing between buttons
        buttonPanel.add(refreshButton);

        // Add the button panel to the bottom of the panel
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void refreshUsersTable() {
        // Clear the table model
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);

        // Get all users from the UserRepository
        List<User> users = userRepository.getAllUsers();

        if (!users.isEmpty()) {
            // Use reflection to get the column names
            Class<?> userClass = users.get(0).getClass();
            for (java.lang.reflect.Field field : userClass.getDeclaredFields()) {
                tableModel.addColumn(field.getName());
            }

            // Populate the table with user details
            for (User user : users) {
                Object[] rowData = new Object[userClass.getDeclaredFields().length];
                int i = 0;
                for (java.lang.reflect.Field field : userClass.getDeclaredFields()) {
                    field.setAccessible(true);
                    try {
                        rowData[i] = field.get(user);
                    } catch (IllegalAccessException ex) {
                        ex.printStackTrace();
                    }
                    i++;
                }
                tableModel.addRow(rowData);
            }
        }
    }
    private void editUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow != -1) {
            // Get the User object corresponding to the selected row
            User user = getUserFromSelectedRow(selectedRow);
            // Open edit user dialog
            EditUserDialog editUserDialog = new EditUserDialog(user);
            editUserDialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Te rog selectează un utilizator pentru a fi editat.");
        }
    }
    private static class EditUserDialog extends JDialog {
        private final JTextField usernameField;
        private final JTextField firstNameField;
        private final JTextField lastNameField;
        private final JTextField passwordField;
        private final JTextField emailField;
        private final JTextField phoneNumberField;
        private final JTextField addressField;
        private final JTextField roleField; // Assuming role is editable too

        private final User user;

        public EditUserDialog(User user) {
            this.user = user;
            setTitle("Editează Utilizator");
            setSize(350, 400); // Adjust size as needed
            setLayout(new GridLayout(9, 2)); // Adjust rows and columns as needed

            // Initialize text fields with user details
            usernameField = new JTextField(user.getUsername());
            firstNameField = new JTextField(user.getFirstName());
            lastNameField = new JTextField(user.getLastName());
            passwordField = new JTextField(user.getPassword());
            emailField = new JTextField(user.getEmail());
            phoneNumberField = new JTextField(user.getPhoneNumber());
            addressField = new JTextField(user.getAddress());
            roleField = new JTextField(user.getRole());

            // Make the username field not editable
            usernameField.setEditable(false);

            // Add text fields to the dialog
            add(new JLabel("Username:"));
            add(usernameField);
            add(new JLabel("First Name:"));
            add(firstNameField);
            add(new JLabel("Last Name:"));
            add(lastNameField);
            add(new JLabel("Password:"));
            add(passwordField);
            add(new JLabel("Email:"));
            add(emailField);
            add(new JLabel("Phone Number:"));
            add(phoneNumberField);
            add(new JLabel("Address:"));
            add(addressField);
            add(new JLabel("Role:"));
            add(roleField);

            // Add "Salvează" button
            JButton saveButton = new JButton("Salvează");
            saveButton.addActionListener(e -> saveChanges());
            add(saveButton,"warp center");

            // Center the dialog on the screen
            setLocationRelativeTo(null);
            setResizable(false);
        }

        private void saveChanges() {
            // Update user details
            user.setUsername(usernameField.getText());
            user.setFirstName(firstNameField.getText());
            user.setLastName(lastNameField.getText());
            user.setPassword(passwordField.getText());
            user.setEmail(emailField.getText());
            user.setPhoneNumber(phoneNumberField.getText());
            user.setAddress(addressField.getText());
            user.setRole(roleField.getText());

            // Update user in the database using UserRepository
            UserRepository userRepository = new UserRepository();
            userRepository.updateByUsername(user.getUsername(), user);

            // Close the dialog
            dispose();
        }
    }



    private void deleteSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow != -1) {
            // Get the User object corresponding to the selected row
            User user = getUserFromSelectedRow(selectedRow);
            int option = JOptionPane.showConfirmDialog(this, "Ești sigur ca vrei să ștergi utilizatorul: " + user.getUsername() + "?", "Confirmă Ștergerea", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                // Delete the user using UserRepository
                userRepository.deleteByUsername(user.getUsername());
                // Refresh the table
                refreshUsersTable();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Te rog selectează un utilizator pentru a fi șters.");
        }
    }

    private User getUserFromSelectedRow(int selectedRow) {
        // Get the User object corresponding to the selected row
        User user = new User();
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            String columnName = tableModel.getColumnName(i);
            Object columnValue = tableModel.getValueAt(selectedRow, i);
            try {
                java.lang.reflect.Field field = User.class.getDeclaredField(columnName);
                field.setAccessible(true);
                if (field.getType().equals(Long.class)) {
                    field.set(user, Long.parseLong(columnValue.toString()));
                } else {
                    field.set(user, columnValue);
                }
            } catch (NoSuchFieldException | IllegalAccessException ex) {

                ex.printStackTrace();
            }
        }
        return user;
    }
}

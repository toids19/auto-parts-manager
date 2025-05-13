/**
 * The AdminListView class represents a JPanel for displaying a list of administrators.
 * It includes functionalities to view, edit, and delete administrators.
 */

// Import statements
package org.example.View;

import org.example.Connection.ConnectionFactory;
import org.example.Model.User;
import org.example.Repository.UserRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

// Suppressing warnings - Avoid suppressing all warnings, use specific ones if needed.
@SuppressWarnings("ALL")
// AdminListView class
public class AdminListView extends JPanel {

    // Class variables
    private DefaultTableModel tableModel;
    private JTable adminTable;
    private final UserRepository userRepository;

    /**
     * Constructs an AdminListView object.
     * Initializes the UserRepository with a database connection and initializes the UI.
     */
    public AdminListView() {
        userRepository = new UserRepository(ConnectionFactory.getConnection());
        initUI();
    }

    /**
     * Initializes the user interface.
     * Sets the layout, creates a table model, populates the table with administrators, and adds buttons for actions.
     */
    private void initUI() {
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel();
        refreshAdminsTable();

        adminTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make cells non-editable
            }
        };
        adminTable.setPreferredScrollableViewportSize(new Dimension(600, 200));

        JScrollPane scrollPane = new JScrollPane(adminTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));

        JButton editButton = new JButton("EDITEAZĂ");
        JButton deleteButton = new JButton("ȘTERGE");
        JButton refreshButton = new JButton("REFRESH");

        editButton.addActionListener(e -> editUser());
        deleteButton.addActionListener(e -> deleteSelectedUser());
        refreshButton.addActionListener(e -> refreshAdminsTable());

        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(refreshButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Refreshes the table with administrators.
     * Retrieves administrators from the UserRepository and populates the table with user details.
     */
    private void refreshAdminsTable() {
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);

        List<User> admins = userRepository.getAllAdmins();

        if (!admins.isEmpty()) {
            Class<?> userClass = admins.get(0).getClass();
            for (java.lang.reflect.Field field : userClass.getDeclaredFields()) {
                tableModel.addColumn(field.getName());
            }

            for (User admin : admins) {
                Object[] rowData = new Object[userClass.getDeclaredFields().length];
                int i = 0;
                for (java.lang.reflect.Field field : userClass.getDeclaredFields()) {
                    field.setAccessible(true);
                    try {
                        rowData[i] = field.get(admin);
                    } catch (IllegalAccessException ex) {
                        ex.printStackTrace();
                    }
                    i++;
                }
                tableModel.addRow(rowData);
            }
        }
    }

    /**
     * Opens the edit user dialog.
     * Retrieves the selected user and opens a dialog to edit user details.
     */
    private void editUser() {
        int selectedRow = adminTable.getSelectedRow();
        if (selectedRow != -1) {
            User user = getUserFromSelectedRow(selectedRow);
            EditUserDialog editUserDialog = new EditUserDialog(user);
            editUserDialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Te rog selectează un utilizator pentru a fi editat.");
        }
    }

    /**
     * Deletes the selected user.
     * Retrieves the selected user and confirms deletion before removing it from the database.
     * Refreshes the table after deletion.
     */
    private void deleteSelectedUser() {
        int selectedRow = adminTable.getSelectedRow();
        if (selectedRow != -1) {
            User user = getUserFromSelectedRow(selectedRow);
            int option = JOptionPane.showConfirmDialog(this, "Ești sigur ca vrei să ștergi administratorul: " + user.getUsername() + "?", "Confirmă Ștergerea", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                userRepository.deleteByUsername(user.getUsername());
                refreshAdminsTable();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Te rog selectează un utilizator pentru a fi șters.");
        }
    }

    /**
     * Retrieves the user from the selected row in the table.
     * Constructs a User object and sets its fields based on the selected row.
     *
     * @param selectedRow The index of the selected row in the table.
     * @return The User object corresponding to the selected row.
     */
    private User getUserFromSelectedRow(int selectedRow) {
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

    /**
     * The EditUserDialog class represents a dialog for editing user details.
     * It allows the user to modify user attributes and save changes to the database.
     */
    private static class EditUserDialog extends JDialog {

        // Text fields for user details
        private final JTextField usernameField;
        private final JTextField firstNameField;
        private final JTextField lastNameField;
        private final JTextField passwordField;
        private final JTextField emailField;
        private final JTextField phoneNumberField;
        private final JTextField addressField;
        private final JTextField roleField;

        private final User user;

        /**
         * Constructs an EditUserDialog object.
         * Initializes the dialog with the specified User object and sets up text fields for user details.
         *
         * @param user The User object to be edited.
         */
        public EditUserDialog(User user) {
            this.user = user;
            setTitle("Editează Utilizator");
            setSize(350, 400);
            setLayout(new GridLayout(9, 2));

            usernameField = new JTextField(user.getUsername());
            firstNameField = new JTextField(user.getFirstName());
            lastNameField = new JTextField(user.getLastName());
            passwordField = new JTextField(user.getPassword());
            emailField = new JTextField(user.getEmail());
            phoneNumberField = new JTextField(user.getPhoneNumber());
            addressField = new JTextField(user.getAddress());
            roleField = new JTextField(user.getRole());

            usernameField.setEditable(false);

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

            JButton saveButton = new JButton("Salvează");
            saveButton.addActionListener(e -> saveChanges());
            add(saveButton, "wrap center");

            setLocationRelativeTo(null);
            setResizable(false);
        }

        /**
         * Saves changes made to the user details.
         * Updates the user object with the modified values and updates the corresponding record in the database.
         */
        private void saveChanges() {
            user.setUsername(usernameField.getText());
            user.setFirstName(firstNameField.getText());
            user.setLastName(lastNameField.getText());
            user.setPassword(passwordField.getText());
            user.setEmail(emailField.getText());
            user.setPhoneNumber(phoneNumberField.getText());
            user.setAddress(addressField.getText());
            user.setRole(roleField.getText());

            UserRepository userRepository = new UserRepository();
            userRepository.updateByUsername(user.getUsername(), user);

            dispose();
        }
    }
}

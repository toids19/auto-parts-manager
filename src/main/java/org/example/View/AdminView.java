/**
 * The AdminView class represents a JPanel for the admin interface.
 * It displays the admin dashboard and provides access to admin functionalities.
 */

// Import statements
package org.example.View;

import org.example.Controller.AdminController;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

/**
 * JPanel subclass representing the admin interface.
 */
// AdminView class
public class AdminView extends JPanel {

    // Constructor

    /**
     * Constructs an AdminView object.
     * Initializes the admin controller with a reference to this view and initializes the UI.
     */
    public AdminView() {
        // Initialize AdminController with reference to this view
        AdminController adminController = new AdminController(this);
        // Initialize the UI
        init();
    }

    // Method to initialize the UI

    /**
     * Initializes the user interface for the admin view.
     * Sets up the layout and components of the admin dashboard.
     */
    private void init() {
        // Set layout using MigLayout with horizontal and vertical fill
        setLayout(new MigLayout("fillx", "[grow,fill]", "[grow,fill]"));

        // Create a panel with MigLayout
        JPanel panel = new JPanel(new MigLayout("fillx", "[center]", "[center]"));
        // Create a header label
        JLabel header = new JLabel("WELCOME TO THE ADMIN VIEW");

        // Add header label to the panel
        panel.add(header);
        // Add the panel to the AdminView
        add(panel);
    }
}

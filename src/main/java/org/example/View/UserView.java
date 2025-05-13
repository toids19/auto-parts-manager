package org.example.View;

// Import statements
import org.example.Controller.UserController;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class UserView extends JPanel {

    /**
     * Constructs a UserView object.
     * Initializes the UserController associated with this view and sets up the UI components.
     */
    public UserView() {
        UserController userController = new UserController(this);
        init();
    }

    /**
     * Initializes the user interface.
     * Sets the layout and adds a header label.
     */
    private void init() {
        setLayout(new MigLayout("fillx", "[grow,fill]", "[grow,fill]"));

        JPanel panel = new JPanel(new MigLayout("fillx", "[center]", "[center]"));
        JLabel header = new JLabel("WELCOME TO THE USER VIEW");

        panel.add(header);
        add(panel);
    }
}

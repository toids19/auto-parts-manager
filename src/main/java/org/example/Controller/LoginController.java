/**
 * The LoginController class is responsible for managing the login process.
 * It interacts with the LoginView to get user input and uses the UserRepository to authenticate the user.
 */
package org.example.Controller;

import org.example.Manager.SessionManager;
import org.example.View.LoginView;
import org.example.Connection.ConnectionFactory;
import org.example.Repository.UserRepository;
import org.example.Model.User;
import raven.toast.Notifications;
import org.example.Main.Main;

import javax.swing.*;

/**
 * The LoginController class handles user authentication.
 */
public class LoginController {

    /**
     * The view associated with the login process.
     */
    private final LoginView loginView;

    /**
     * The repository for interacting with the user database.
     */
    private final UserRepository userRepository;

    /**
     * Constructor for LoginController.
     *
     * @param loginView The view associated with this controller.
     */
    public LoginController(LoginView loginView) {
        this.loginView = loginView;
        this.userRepository = new UserRepository(ConnectionFactory.getConnection());
        /*
          The repository for interacting with the admin database.
         */
        UserRepository adminRepository = new UserRepository(ConnectionFactory.getConnection());
    }

    /**
     * Handles the login process.
     * Checks if the provided username and password match an admin or user account.
     */
    public void login() {
        String username = loginView.getTxtUsername().getText();
        String password = new String(loginView.getTxtPassword().getPassword());

        // Check if it is an admin login
        User admin = userRepository.getUserByUsernamePasswordAndRole(username, password, "ADMINISTRATOR");
        if (admin != null) {
            // Admin logged in, you can open the admin interface here
            Main.loginAdmin();
            SessionManager.setLoggedInAdmin(admin);
        } else {
            // Check if it is a user login
            User user = userRepository.getUserByUsernamePasswordAndRole(username, password, "CLIENT");
            if (user != null) {
                // User logged in, you can open the user interface here
                SessionManager.setLoggedInUser(user);
                Main.loginUser();
            } else {
                // Invalid credentials
                JOptionPane.showMessageDialog(loginView, "Nume Utilizator sau parolă incorecte!", "Eroare!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

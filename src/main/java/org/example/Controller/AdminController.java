/**
 * The AdminController class is responsible for managing the interactions
 * between the AdminView and the UserRepository.
 */
package org.example.Controller;

import org.example.Connection.ConnectionFactory;
import org.example.Repository.UserRepository;
import org.example.View.AdminView;

import java.sql.SQLException;

/**
 * The AdminController class initializes the controller with the given AdminView.
 */
public class AdminController {

    /**
     * Constructor for the AdminController class.
     *
     * @param adminView The view that the controller will interact with.
     */
    public AdminController(AdminView adminView) {
        // Create a UserRepository instance using a connection from ConnectionFactory
        UserRepository adminRepository = new UserRepository(ConnectionFactory.getConnection());
    }
}

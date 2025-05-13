/**
 * The UserController class is responsible for managing the interaction
 * between the UserView and the UserRepository.
 */
package org.example.Controller;

import org.example.Connection.ConnectionFactory;
import org.example.Repository.UserRepository;
import org.example.View.UserView;

/**
 * The UserController class handles user-related operations.
 */
public class UserController {

    /**
     * Constructor for UserController.
     * Initializes the UserRepository with a database connection.
     *
     * @param userView The view associated with this controller.
     */
    public UserController(UserView userView) {
        UserRepository userRepository = new UserRepository(ConnectionFactory.getConnection());
    }
}

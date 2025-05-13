package org.example.Repository.Test;

import org.example.Repository.UserRepository;
import org.example.Model.User;
import org.example.Connection.ConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * A class for testing the UserRepository class.
 */
public class UserRepositoryTest {

    /**
     * The main method to run the test cases.
     *
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {
        // Create a UserRepository instance
        UserRepository userDAO = new UserRepository();

        // Test insert: Create a new user
        // Execute SQL query to insert the new user into the database
        String createUserQuery = "INSERT INTO PT_DATABASE.USERS (username, password, first_name, last_name, email, phone_number, address, role) " +
                "VALUES ('newuser', 'password', 'New', 'User', 'newuser@example.com', '1234567890', '123 New Street', 'CLIENT')";

        // Initialize Connection and Statement variables
        Connection connection = null;
        Statement statement = null;

        try {
            // Get a database connection using ConnectionFactory
            connection = ConnectionFactory.getConnection();

            // Create a statement object for executing queries
            statement = connection.createStatement();

            // Execute the SQL query to insert the new user into the database
            int rowsAffected = statement.executeUpdate(createUserQuery);

            // Print the number of rows affected (should be 1 if the user was inserted successfully)
            System.out.println("Rows affected by user creation: " + rowsAffected);

        } catch (SQLException e) {
            // Handle any SQL exceptions
            e.printStackTrace();
        } finally {
            // Close the statement and connection in a finally block to ensure they're always closed
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }

        // Test insert: Find the newly created user by username
        User foundUser = userDAO.findByUsername("newuser");
        System.out.println("User found by username: " + foundUser);

        // Test updateByUsername
        // Update the user's email
        if (foundUser != null) {
            foundUser.setEmail("updatedemail@example.com");
            User updatedUser = userDAO.updateByUsername("newuser", foundUser);
            System.out.println("User after update: " + updatedUser);
        } else {
            System.out.println("User not found for update.");
        }

        // Test deleteByUsername
        // Delete the user by username
        int deletedRows = userDAO.deleteByUsername("newuser");
        System.out.println("Deleted rows: " + deletedRows);
    }
}

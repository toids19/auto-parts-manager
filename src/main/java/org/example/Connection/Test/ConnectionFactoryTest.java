/**
 * The ConnectionFactoryTest class is used to test the functionality of the ConnectionFactory class.
 */
package org.example.Connection.Test;

import org.example.Connection.ConnectionFactory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * A class for testing the ConnectionFactory class.
 */
public class ConnectionFactoryTest {

    /**
     * Main method to test the ConnectionFactory class.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        // Declaration of database resources
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // Get a connection to the database using ConnectionFactory
            connection = ConnectionFactory.getConnection();

            // Check if the connection is established
            if (connection != null) {
                System.out.println("Connection established successfully.");

                // Create a statement object
                statement = connection.createStatement();

                // Execute a query and get a result set
                String query = "SELECT * FROM PRODUCTS";
                resultSet = statement.executeQuery(query);

                // Process the result set
                while (resultSet.next()) {
                    // Retrieve data from the result set
                    int id = resultSet.getInt("product_id");
                    String name = resultSet.getString("product_name");
                    // Print retrieved data
                    System.out.println("ID: " + id + ", Name: " + name);
                }
            } else {
                // Print a message if connection fails
                System.out.println("Failed to establish a connection.");
            }
        } catch (SQLException e) {
            // Print the stack trace if a SQL exception occurs
            e.printStackTrace();
        } finally {
            // Close the resources in a finally block to ensure they are closed even if an exception occurs
            // Calling close methods from ConnectionFactory to handle resource closure
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }
}

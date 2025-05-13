package org.example.Repository.Test;

import org.example.Repository.OrderRepository;
import org.example.Model.Order;
import org.example.Connection.ConnectionFactory;

import java.util.List;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * A class for testing the OrderRepository class.
 */
public class OrderRepositoryTest {

    /**
     * The main method to run the test cases.
     *
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {
        // Create an instance of OrderRepository
        OrderRepository orderDAO = new OrderRepository();

        // Test insert: Create a new order
        // Execute SQL query to insert the new order into the database
        String createOrderQuery = "INSERT INTO PT_DATABASE.ORDERS (user_id, product_id, quantity, total_price) " +
                "VALUES (1, 1, 2, 100.00)";

        // Initialize Connection and Statement variables
        Connection connection = null;
        Statement statement = null;

        try {
            // Get a database connection using ConnectionFactory
            connection = ConnectionFactory.getConnection();

            // Create a statement object for executing queries
            statement = connection.createStatement();

            // Execute the SQL query to insert the new order into the database
            int rowsAffected = statement.executeUpdate(createOrderQuery);

            // Print the number of rows affected (should be 1 if the order was inserted successfully)
            System.out.println("Rows affected by order creation: " + rowsAffected);

        } catch (SQLException e) {
            // Handle any SQL exceptions
            e.printStackTrace();
        } finally {
            // Close the statement and connection in a finally block to ensure they're always closed
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }

        // Test findOrdersByUserId
        Long userId = 1L;
        // Retrieve orders by user ID
        List<Order> ordersByUserId = orderDAO.findOrdersByUserId(userId);
        if (ordersByUserId != null) {
            // Print orders found for the user ID
            System.out.println("Orders found by user ID " + userId + ":");
            for (Order order : ordersByUserId) {
                System.out.println(order);
            }
        } else {
            // Print message if no orders found for the user ID
            System.out.println("No orders found for user ID " + userId);
        }

        // Test deleteOrdersWithProductId
        Long productId = 1L;
        // Delete orders with the specified product ID
        int deletedRows = orderDAO.deleteOrdersWithProductId(productId);
        // Print the number of deleted rows
        System.out.println("Deleted " + deletedRows + " orders with product ID " + productId);

        // Test deleteOrdersWithProductName
        String productName = "ExampleProduct";
        // Delete orders with the specified product name
        deletedRows = orderDAO.deleteOrdersWithProductName(productName);
        // Print the number of deleted rows
        System.out.println("Deleted " + deletedRows + " orders with product name " + productName);
    }
}

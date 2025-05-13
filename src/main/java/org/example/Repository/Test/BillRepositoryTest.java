package org.example.Repository.Test;

import org.example.Repository.BillRepository;
import org.example.Model.Bill;
import org.example.Connection.ConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

/**
 * A class for testing the BillRepository class.
 */
public class BillRepositoryTest {

    /**
     * The main method to run the test cases.
     *
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {
        // Creating an instance of BillRepository
        BillRepository billDAO = new BillRepository();

        // Test insertion of a bill
        Timestamp createdAt = new Timestamp(System.currentTimeMillis()); // Current timestamp
        Bill bill = new Bill(1L, "Bosch Platinum Spark Plugs", 2, 25.98, "cretuelena", "Elena", "Cretu", "Calea Nationala 72", "0748925925", createdAt);

        // Execute SQL query to insert the new bill into the database
        String insertBillQuery = "INSERT INTO PT_DATABASE.BILLS (order_id, product_name, quantity, total_price, username, first_name, last_name, address, phone_number, created_at) " +
                "VALUES (1, 'Bosch Platinum Spark Plugs', 2, 25.98, 'cretuelena', 'Elena', 'Cretu', 'Calea Nationala 72', '0748925925', '" + createdAt + "')";

        // Initialize Connection and Statement variables
        Connection connection = null;
        Statement statement = null;

        try {
            // Get a database connection using ConnectionFactory
            connection = ConnectionFactory.getConnection();

            // Create a statement object for executing queries
            statement = connection.createStatement();

            // Execute the SQL query to insert the new bill into the database
            int rowsAffected = statement.executeUpdate(insertBillQuery);

            // Print the number of rows affected (should be 1 if the bill was inserted successfully)
            System.out.println("Rows affected by bill insertion: " + rowsAffected);

        } catch (SQLException e) {
            // Handle any SQL exceptions
            e.printStackTrace();
        } finally {
            // Close the statement and connection in a finally block to ensure they're always closed
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }

        // Test finding a bill by ID
        Long id = 11L; // Change this to an existing ID in your database
        // Finding a bill by ID
        Bill foundBill = billDAO.findBillById(id);
        if (foundBill != null) {
            // Printing the found bill if it exists
            System.out.println("Found Bill: " + foundBill);
        } else {
            // Printing a message if the bill is not found
            System.out.println("Bill not found for ID: " + id);
        }

        // Test deleting a bill by ID
        // Deleting a bill by ID
        int rowsDeleted = billDAO.deleteBillById(id);
        // Printing the number of rows deleted
        System.out.println("Deleted " + rowsDeleted + " row(s) with ID: " + id);
    }
}

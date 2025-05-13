package org.example.Repository.Test;

import org.example.Repository.ProductRepository;
import org.example.Model.Product;
import org.example.Connection.ConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * A class for testing the ProductRepository class.
 */
public class ProductRepositoryTest {

    /**
     * The main method to run the test cases.
     *
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {
        // Create a ProductRepository instance
        ProductRepository productDAO = new ProductRepository(ConnectionFactory.getConnection());

        // Test insert: Create a new product
        // Execute SQL query to insert the new product into the database
        String createProductQuery = "INSERT INTO PT_DATABASE.PRODUCTS (product_name, product_description, product_price, product_stock) " +
                "VALUES ('New Product', 'New Product Description', 99.99, 100)";

        // Initialize Connection and Statement variables
        Connection connection = null;
        Statement statement = null;

        try {
            // Get a database connection using ConnectionFactory
            connection = ConnectionFactory.getConnection();

            // Create a statement object for executing queries
            statement = connection.createStatement();

            // Execute the SQL query to insert the new product into the database
            int rowsAffected = statement.executeUpdate(createProductQuery);

            // Print the number of rows affected (should be 1 if the product was inserted successfully)
            System.out.println("Rows affected by product creation: " + rowsAffected);

        } catch (SQLException e) {
            // Handle any SQL exceptions
            e.printStackTrace();
        } finally {
            // Close the statement and connection in a finally block to ensure they're always closed
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }

        // Test findByProductName
        // Find a product by its name
        Product foundProduct = productDAO.findByProductName("New Product");
        System.out.println("Product found by product name: " + foundProduct);

        // Test updateByProductName
        // Update the product's description
        if (foundProduct != null) {
            foundProduct.setProductDescription("Updated description for New Product");
            Product updatedProduct = productDAO.updateByProductName("New Product", foundProduct);
            System.out.println("Product after update: " + updatedProduct);
        } else {
            System.out.println("Product not found for update.");
        }

        // Test deleteByProductName
        // Delete a product by its name
        int deletedRows = productDAO.deleteByProductName("New Product");
        System.out.println("Deleted rows: " + deletedRows);
    }
}

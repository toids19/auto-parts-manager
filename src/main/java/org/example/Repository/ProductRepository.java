package org.example.Repository;

import org.example.Connection.ConnectionFactory;
import org.example.Mapper.FieldMapper;
import org.example.Model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Data Access Object (DAO) for handling Product entities.
 */
public class ProductRepository extends AbstractRepository<Product> {

    private final Connection connection;

    /**
     * Constructor with connection parameter.
     *
     * @param connection The database connection.
     */
    public ProductRepository(Connection connection) {
        this.connection = connection;
    }

    /**
     * Create a new product.
     *
     * @param product The product object to be created.
     */
    public void createProduct(Product product) {
        String query = "INSERT INTO products (product_name, product_description, product_price, product_stock) VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, product.getProductName());
            preparedStatement.setString(2, product.getProductDescription());
            preparedStatement.setDouble(3, product.getProductPrice());
            preparedStatement.setInt(4, product.getProductStock());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.severe("Error occurred while creating product: " + e.getMessage());
        }
    }

    /**
     * Check if a product with the given name exists.
     *
     * @param productName The name of the product.
     * @return True if the product exists, false otherwise.
     */
    public boolean existsByProductName(String productName) {
        String query = "SELECT 1 FROM products WHERE product_name = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, productName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            LOGGER.severe("Error occurred while checking if product exists: " + e.getMessage());
        }

        return false;
    }

    /**
     * Find a product by its name.
     *
     * @param productName The name of the product.
     * @return The product object if found, null otherwise.
     */
    public Product findByProductName(String productName) {
        String query = createFindByProductNameQuery(productName);
        return executeQueryForSingleResult(query);
    }

    /**
     * Update a product by its name.
     *
     * @param productName    The name of the product.
     * @param updatedProduct The updated product object.
     * @return The updated product object.
     */
    public Product updateByProductName(String productName, Product updatedProduct) {
        String query = createUpdateByProductNameQuery(productName, updatedProduct);
        return executeUpdateByProductName(query);
    }

    /**
     * Delete a product by its name.
     *
     * @param productName The name of the product to be deleted.
     * @return The number of rows affected.
     */
    public int deleteByProductName(String productName) {
        String query = createDeleteByProductNameQuery(productName);
        return executeDeleteByProductName(query);
    }

    /**
     * Generate SQL query to find a product by its name.
     *
     * @param productName The name of the product.
     * @return The SQL query.
     */
    private String createFindByProductNameQuery(String productName) {
        return String.format("SELECT * FROM products WHERE product_name = '%s';", productName);
    }

    /**
     * Generate SQL query to update a product by its name.
     *
     * @param productName    The name of the product.
     * @param updatedProduct The updated product object.
     * @return The SQL query.
     */
    private String createUpdateByProductNameQuery(String productName, Product updatedProduct) {
        StringBuilder setClause = new StringBuilder();

        // Construct the SET clause for the update query
        for (java.lang.reflect.Field field : Product.class.getDeclaredFields()) {
            if (field.getName().equals("productId")) {
                continue;
            }
            field.setAccessible(true);
            try {
                String columnName = FieldMapper.mapFieldNameToColumnName(field.getName());
                setClause.append(columnName).append("='").append(field.get(updatedProduct)).append("',");
            } catch (IllegalAccessException e) {
                LOGGER.severe("Error accessing field value: " + e.getMessage());
            }
        }
        setClause.setLength(setClause.length() - 1); // Remove the trailing comma

        return String.format("UPDATE products SET %s WHERE product_name = '%s';", setClause, productName);
    }

    /**
     * Generate SQL query to delete a product by its name.
     *
     * @param productName The name of the product.
     * @return The SQL query.
     */
    private String createDeleteByProductNameQuery(String productName) {
        return String.format("DELETE FROM products WHERE product_name = '%s';", productName);
    }

    /**
     * Get all products.
     *
     * @return A list of all products.
     */
    public ArrayList<Product> getAllProducts() {
        String query = "SELECT * FROM products";
        return (ArrayList<Product>) executeQueryForMultipleResults(query);
    }

    /**
     * Map data from ResultSet to a Product object.
     *
     * @param resultSet The ResultSet containing product data.
     * @return The Product object.
     * @throws SQLException If an SQL error occurs.
     */
    private Product mapFromResultSet(ResultSet resultSet) throws SQLException {
        Product product = new Product();

        // Set properties of the Product object from the ResultSet
        product.setProductId(resultSet.getLong("product_id"));
        product.setProductName(resultSet.getString("product_name"));
        product.setProductDescription(resultSet.getString("product_description"));
        product.setProductPrice(resultSet.getDouble("product_price"));
        product.setProductStock(resultSet.getInt("product_stock"));

        return product;
    }

    /**
     * Execute update query for product by its name and return the updated product.
     *
     * @param query The SQL update query.
     * @return The updated product object.
     */
    private Product executeUpdateByProductName(String query) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            int affectedRows = preparedStatement.executeUpdate();

            // Check if any rows were affected by the update
            if (affectedRows == 0) {
                LOGGER.severe("Updating product by product name failed, no rows affected.");
                return null;
            }

            // Return the updated product
            return findByProductName(query.split("'")[1]);
        } catch (SQLException e) {
            LOGGER.severe("Error occurred while updating product by product name: " + e.getMessage());
        }

        return null;
    }

    /**
     * Execute delete query for product by its name and return the number of affected rows.
     *
     * @param query The SQL delete query.
     * @return The number of affected rows.
     */
    private int executeDeleteByProductName(String query) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.severe("Error occurred while deleting product by product name: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Create a Product object from the ResultSet.
     *
     * @param resultSet The ResultSet containing product data.
     * @return The Product object.
     */
    protected Product createObject(ResultSet resultSet) {
        try {
            if (resultSet.next()) {
                return mapFromResultSet(resultSet);
            }
        }
        catch (SQLException e) {
            LOGGER.severe("Error creating object: " + e.getMessage());
        }
        return null;
    }

    /**
     * Get a product by its ID.
     *
     * @param productId The ID of the product.
     * @return The product object if found, null otherwise.
     */
    public Product getProductById(Long productId) {
        String query = "SELECT * FROM products WHERE product_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, productId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapFromResultSet(resultSet);
                } else {
                    LOGGER.warning("No product found with ID: " + productId);
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("Error occurred while getting product by ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Update a product.
     *
     * @param product The product object to be updated.
     */
    public void updateProduct(Product product) {
        String query = "UPDATE products SET product_name = ?, product_description = ?, product_price = ?, product_stock = ? WHERE product_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, product.getProductName());
            preparedStatement.setString(2, product.getProductDescription());
            preparedStatement.setDouble(3, product.getProductPrice());
            preparedStatement.setInt(4, product.getProductStock());
            preparedStatement.setLong(5, product.getProductId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.severe("Error occurred while updating product: " + e.getMessage());
        }
    }

}

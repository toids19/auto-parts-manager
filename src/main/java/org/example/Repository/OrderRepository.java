package org.example.Repository;

import org.example.Connection.ConnectionFactory;
import org.example.Manager.SessionManager;
import org.example.Model.Order;
import org.example.Model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Data Access Object (DAO) for handling Order entities.
 */
public class OrderRepository extends AbstractRepository<Order> {

    private static final Logger LOGGER = Logger.getLogger(OrderRepository.class.getName());

    /**
     * Constructor for OrderRepository.
     */
    public OrderRepository() {
    }

    /**
     * Find orders by user ID.
     *
     * @param userId The ID of the user.
     * @return A list of orders.
     */
    public List<Order> findOrdersByUserId(Long userId) {
        String query = "SELECT * FROM orders WHERE user_id = ?";
        return executeQueryWithParameter(query, userId);
    }

    /**
     * Delete orders with a specific product ID.
     *
     * @param productId The ID of the product.
     * @return The number of affected rows.
     */
    public int deleteOrdersWithProductId(Long productId) {
        String query = "DELETE FROM orders WHERE product_id IN (SELECT product_id FROM products WHERE product_id = ?)";
        return executeUpdateWithParameter(query, productId);
    }

    /**
     * Delete orders with a specific product name.
     *
     * @param productName The name of the product.
     * @return The number of affected rows.
     */
    public int deleteOrdersWithProductName(String productName) {
        String query = "DELETE FROM orders WHERE product_id IN (SELECT product_id FROM products WHERE products.name = ?)";
        return executeUpdateWithParameter(query, productName);
    }

    /**
     * Place an order.
     *
     * @param productId   The ID of the product.
     * @param quantity    The quantity of the product.
     * @param totalPrice  The total price of the order.
     * @return The ID of the placed order.
     */
    public Long placeOrder(Long productId, int quantity, double totalPrice) {
        Long orderId = null;
        Long userId = SessionManager.getLoggedInUser().getUserId();
        String query = "INSERT INTO orders (user_id, product_id, quantity, total_price, created_at) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, productId);
            preparedStatement.setInt(3, quantity);
            preparedStatement.setDouble(4, totalPrice);
            preparedStatement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        orderId = generatedKeys.getLong(1);
                    }
                }
                ProductRepository productRepository = new ProductRepository(ConnectionFactory.getConnection());
                Product product = productRepository.getProductById(productId);
                if (product != null) {
                    int updatedStock = product.getProductStock() - quantity;
                    product.setProductStock(updatedStock);
                    productRepository.updateProduct(product);
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("Error occurred while placing order: " + e.getMessage());
        }
        return orderId;
    }

    /**
     * Create an Order object from a ResultSet.
     *
     * @param resultSet The ResultSet containing order data.
     * @return The created Order object.
     */
    protected Order createObject(ResultSet resultSet) {
        try {
            if (resultSet.next()) {
                return mapFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            LOGGER.severe("Error creating object: " + e.getMessage());
        }
        return null;
    }

    /**
     * Execute a query with a parameter and return a list of orders.
     *
     * @param query     The SQL query.
     * @param parameter The parameter for the query.
     * @return A list of orders.
     */
    private List<Order> executeQueryWithParameter(String query, Object parameter) {
        List<Order> orderList = new ArrayList<>();
        try (PreparedStatement preparedStatement = ConnectionFactory.getConnection().prepareStatement(query)) {
            if (parameter instanceof Long) {
                preparedStatement.setLong(1, (Long) parameter);
            } else if (parameter instanceof String) {
                preparedStatement.setString(1, (String) parameter);
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                orderList.add(mapFromResultSet(resultSet));
            }
            return orderList;
        } catch (SQLException e) {
            LOGGER.severe("Error occurred while executing query with parameter: " + e.getMessage());
        }
        return null;
    }

    /**
     * Execute an update query with a parameter and return the number of affected rows.
     *
     * @param query     The SQL query.
     * @param parameter The parameter for the query.
     * @return The number of affected rows.
     */
    private int executeUpdateWithParameter(String query, Object parameter) {
        try (PreparedStatement preparedStatement = ConnectionFactory.getConnection().prepareStatement(query)) {
            if (parameter instanceof Long) {
                preparedStatement.setLong(1, (Long) parameter);
            } else if (parameter instanceof String) {
                preparedStatement.setString(1, (String) parameter);
            }
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.severe("Error occurred while executing update with parameter: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Map a ResultSet row to an Order object.
     *
     * @param resultSet The ResultSet containing order data.
     * @return The mapped Order object.
     * @throws SQLException If an SQL exception occurs.
     */
    private Order mapFromResultSet(ResultSet resultSet) throws SQLException {
        Order order = new Order();
        order.setOrderId(resultSet.getLong("order_id"));
        order.setUserId(resultSet.getLong("user_id"));
        order.setProductId(resultSet.getLong("product_id"));
        order.setQuantity(resultSet.getInt("quantity"));
        order.setTotalPrice(resultSet.getDouble("total_price"));
        order.setCreatedAt(resultSet.getTimestamp("created_at"));
        return order;
    }
}

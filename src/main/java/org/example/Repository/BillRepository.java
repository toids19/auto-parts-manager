package org.example.Repository;

import org.example.Connection.ConnectionFactory;
import org.example.Manager.SessionManager;
import org.example.Model.Bill;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for handling Bill entities.
 */
public class BillRepository {

    /**
     * Insert a new bill into the database.
     *
     * @param bill The bill to insert.
     * @return The inserted bill, or null if insertion fails.
     */
    public Bill insertBill(Bill bill) {
        String query = createInsertBillQuery(bill);

        try (PreparedStatement preparedStatement = ConnectionFactory.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            int affectedRows = preparedStatement.executeUpdate();

            // Check if insertion was successful
            if (affectedRows == 0) {
                return null;
            }

            // Retrieve the generated ID and return the inserted bill
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    Long id = generatedKeys.getLong(1);
                    return findBillById(id);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Consider logging the exception instead of printing stack trace
        }

        return null;
    }

    /**
     * Find a bill by its ID.
     *
     * @param id The ID of the bill to find.
     * @return The found bill, or null if not found.
     */
    public Bill findBillById(Long id) {
        String query = createFindBillByIdQuery(id);

        try (PreparedStatement preparedStatement = ConnectionFactory.getConnection().prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            // If a bill with the given ID is found, create and return a Bill object
            if (resultSet.next()) {
                return new Bill(
                        resultSet.getLong(1),
                        resultSet.getString(2),
                        resultSet.getInt(3),
                        resultSet.getDouble(4),
                        resultSet.getString(5),
                        resultSet.getString(6),
                        resultSet.getString(7),
                        resultSet.getString(8),
                        resultSet.getString(9),
                        resultSet.getTimestamp(10)
                );
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Consider logging the exception instead of printing stack trace
        }

        return null;
    }

    /**
     * Delete a bill by its ID.
     *
     * @param id The ID of the bill to delete.
     * @return The number of affected rows.
     */
    public int deleteBillById(Long id) {
        String query = createDeleteBillByIdQuery(id);

        try (PreparedStatement preparedStatement = ConnectionFactory.getConnection().prepareStatement(query)) {
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Consider logging the exception instead of printing stack trace
        }

        return 0;
    }

    /**
     * Generate SQL query to insert a bill into the database.
     *
     * @param bill The bill to insert.
     * @return The SQL insert query.
     */
    private String createInsertBillQuery(Bill bill) {
        return "INSERT INTO bills (order_id, product_name, quantity, total_price, username, first_name, last_name, address, phone_number, created_at) VALUES (" +
                bill.getOrderId() + ", " +
                "'" + bill.getProductName() + "', " +
                bill.getQuantity() + ", " +
                bill.getTotalPrice() + ", " +
                "'" + bill.getUsername() + "', " +
                "'" + bill.getFirstName() + "', " +
                "'" + bill.getLastName() + "', " +
                "'" + bill.getAddress() + "', " +
                "'" + bill.getPhoneNumber() + "', " +
                "'" + bill.getCreatedAt() + "');";
    }

    /**
     * Generate SQL query to find a bill by its ID.
     *
     * @param id The ID of the bill to find.
     * @return The SQL select query.
     */
    private String createFindBillByIdQuery(Long id) {
        return "SELECT * FROM bills WHERE order_id = " +
                id +
                ";";
    }

    /**
     * Generate SQL query to delete a bill by its ID.
     *
     * @param id The ID of the bill to delete.
     * @return The SQL delete query.
     */
    private String createDeleteBillByIdQuery(Long id) {
        return "DELETE FROM bills WHERE order_id = " +
                id +
                ";";
    }

    /**
     * Get all bills for the currently logged-in user.
     *
     * @return A list of bills belonging to the current user.
     */
    public List<Bill> getAllBillsForCurrentUser() {
        List<Bill> bills = new ArrayList<>();
        String query = "SELECT * FROM bills WHERE username = ?";

        try (PreparedStatement preparedStatement = ConnectionFactory.getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, SessionManager.getLoggedInUser().getUsername());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Bill bill = new Bill(
                        resultSet.getLong("order_id"),
                        resultSet.getString("product_name"),
                        resultSet.getInt("quantity"),
                        resultSet.getDouble("total_price"),
                        resultSet.getString("username"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("address"),
                        resultSet.getString("phone_number"),
                        resultSet.getTimestamp("created_at")
                );
                bills.add(bill);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Consider logging the exception instead of printing stack trace
        }

        return bills;
    }

    /**
     * Get all bills from the database.
     *
     * @return A list of all bills.
     */
    public List<Bill> getAllBills() {
        List<Bill> bills = new ArrayList<>();
        String query = "SELECT * FROM bills";

        try (PreparedStatement preparedStatement = ConnectionFactory.getConnection().prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Bill bill = new Bill(
                        resultSet.getLong("order_id"),
                        resultSet.getString("product_name"),
                        resultSet.getInt("quantity"),
                        resultSet.getDouble("total_price"),
                        resultSet.getString("username"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("address"),
                        resultSet.getString("phone_number"),
                        resultSet.getTimestamp("created_at")
                );
                bills.add(bill);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Consider logging the exception instead of printing stack trace
        }
        return bills;
    }
}

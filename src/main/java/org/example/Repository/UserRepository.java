package org.example.Repository;

import org.example.Connection.ConnectionFactory;
import org.example.Mapper.FieldMapper;
import org.example.Model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for handling User entities.
 */
public class UserRepository extends AbstractRepository<User> {

    /**
     * Default constructor.
     */
    public UserRepository() {
    }

    private Connection connection;

    /**
     * Constructor with connection parameter.
     *
     * @param connection The database connection.
     */
    public UserRepository(Connection connection) {
        this.connection = connection;
    }

    /**
     * Find a user by their username.
     *
     * @param username The username of the user.
     * @return The user object if found, null otherwise.
     */
    public User findByUsername(String username) {
        String query = createFindByUsernameQuery(username);
        return executeQueryForSingleResult(query);
    }

    /**
     * Update a user by their username.
     *
     * @param username    The username of the user to be updated.
     * @param updatedUser The updated user object.
     * @return The updated user object.
     */
    public User updateByUsername(String username, User updatedUser) {
        String query = createUpdateByUsernameQuery(username, updatedUser);
        return executeUpdateByUsername(query);
    }

    /**
     * Delete a user by their username.
     *
     * @param username The username of the user to be deleted.
     * @return The number of rows affected.
     */
    public int deleteByUsername(String username) {
        String query = createDeleteByUsernameQuery(username);
        return executeDeleteByUsername(query);
    }

    /**
     * Generate SQL query to find a user by their username.
     *
     * @param username The username of the user.
     * @return The SQL query.
     */
    private String createFindByUsernameQuery(String username) {
        return String.format("SELECT * FROM users WHERE username = '%s';", username);
    }

    /**
     * Generate SQL query to update a user by their username.
     *
     * @param username    The username of the user.
     * @param updatedUser The updated user object.
     * @return The SQL query.
     */
    private String createUpdateByUsernameQuery(String username, User updatedUser) {
        StringBuilder setClause = new StringBuilder();

        // Construct the SET clause for the update query
        for (java.lang.reflect.Field field : User.class.getDeclaredFields()) {
            if (field.getName().equals("userId")) {
                continue;
            }
            field.setAccessible(true);
            try {
                String columnName = FieldMapper.mapFieldNameToColumnName(field.getName());
                setClause.append(columnName).append("='").append(field.get(updatedUser)).append("',");
            } catch (IllegalAccessException e) {
                LOGGER.severe("Error accessing field value: " + e.getMessage());
            }
        }
        setClause.setLength(setClause.length() - 1); // Remove the trailing comma

        return String.format("UPDATE users SET %s WHERE username = '%s';", setClause, username);
    }

    /**
     * Generate SQL query to delete a user by their username.
     *
     * @param username The username of the user.
     * @return The SQL query.
     */
    private String createDeleteByUsernameQuery(String username) {
        return String.format("DELETE FROM users WHERE username = '%s';", username);
    }

    /**
     * Map data from ResultSet to a User object.
     *
     * @param resultSet The ResultSet containing user data.
     * @return The User object.
     * @throws SQLException If an SQL error occurs.
     */
    private User mapFromResultSet(ResultSet resultSet) throws SQLException {
        User user = new User();

        // Set properties of the User object from the ResultSet
        user.setUserId(resultSet.getLong("user_id"));
        user.setFirstName(resultSet.getString("first_name"));
        user.setLastName(resultSet.getString("last_name"));
        user.setUsername(resultSet.getString("username"));
        user.setPassword(resultSet.getString("password"));
        user.setEmail(resultSet.getString("email"));
        user.setPhoneNumber(resultSet.getString("phone_number"));
        user.setAddress(resultSet.getString("address"));
        user.setRole(resultSet.getString("role"));

        return user;
    }

    /**
     * Execute update query for user by their username and return the updated user.
     *
     * @param query The SQL update query.
     * @return The updated user object.
     */
    private User executeUpdateByUsername(String query) {
        try (PreparedStatement preparedStatement = ConnectionFactory.getConnection().prepareStatement(query)) {
            int affectedRows = preparedStatement.executeUpdate();

            // Check if any rows were affected by the update
            if (affectedRows == 0) {
                LOGGER.severe("Updating user by username failed, no rows affected.");
                return null;
            }

            // Return the updated user
            return findByUsername(query.split("'")[1]);
        } catch (SQLException e) {
            LOGGER.severe("Error occurred while updating user by username: " + e.getMessage());
        }

        return null;
    }

    /**
     * Execute delete query for user by their username and return the number of affected rows.
     *
     * @param query The SQL delete query.
     * @return The number of affected rows.
     */
    private int executeDeleteByUsername(String query) {
        try (PreparedStatement preparedStatement = ConnectionFactory.getConnection().prepareStatement(query)) {
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.severe("Error occurred while deleting user by username: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Create a User object from the ResultSet.
     *
     * @param resultSet The ResultSet containing user data.
     * @return The User object.
     */
    protected User createObject(ResultSet resultSet) {
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
     * Get all administrators from the database.
     *
     * @return A list of administrator users.
     */
    public List<User> getAllAdmins() {
        List<User> admins = new ArrayList<>();
        String query = "SELECT * FROM users WHERE role = 'ADMINISTRATOR';";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                User admin = mapFromResultSet(resultSet);
                admins.add(admin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return admins;
    }

    /**
     * Get all users from the database.
     *
     * @return A list of all users.
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users WHERE role = 'CLIENT';";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                User user = mapFromResultSet(resultSet);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Create a new user in the database.
     *
     * @param user The user object to be created.
     */
    public void
    createUser(User user) {
        String query = "INSERT INTO users (first_name, last_name, username, password, email, phone_number, address, role) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Set values for the PreparedStatement
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getUsername());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setString(5, user.getEmail());
            preparedStatement.setString(6, user.getPhoneNumber());
            preparedStatement.setString(7, user.getAddress());
            preparedStatement.setString(8, user.getRole());

            // Execute the insert query
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get a user by username, password, and role.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @param role     The role of the user.
     * @return The user object if found, null otherwise.
     */
    public User getUserByUsernamePasswordAndRole(String username, String password, String role) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ? AND role = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, role);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Check if a user exists by username.
     *
     * @param username The username to check.
     * @return True if the user exists, false otherwise.
     */
    public boolean existsByUsername(String username) {
        String query = "SELECT COUNT(*) FROM USERS WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception
        }
        return false;
    }
}

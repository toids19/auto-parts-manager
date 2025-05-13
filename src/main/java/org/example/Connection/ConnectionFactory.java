/**
 * The ConnectionFactory class manages database connections.
 * It provides methods to create, retrieve, and close database connections,
 * as well as SQL statements and ResultSets.
 */
package org.example.Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The ConnectionFactory class is responsible for managing database connections.
 * It provides methods to create, retrieve, and close database connections,
 * as well as SQL statements and ResultSets.
 */
public class ConnectionFactory {

    /**
     * Logger for logging errors and warnings.
     */
    private static final Logger LOGGER = Logger.getLogger(ConnectionFactory.class.getName());

    /**
     * Database connection details.
     */
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DBURL = "jdbc:mysql://127.0.0.1:3306/PT_DATABASE";
    private static final String USER = "root";
    private static final String PASS = "tudor221906@@";

    /**
     * Singleton instance of the ConnectionFactory.
     */
    private static final ConnectionFactory singleInstance = new ConnectionFactory();

    /**
     * Private constructor to prevent instantiation of the class.
     */
    private ConnectionFactory() {
        try {
            // Load the MySQL JDBC driver
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            // Log and print the exception if the driver is not found
            LOGGER.log(Level.SEVERE, "MySQL JDBC Driver not found!", e);
        }
    }

    /**
     * Creates a database connection.
     *
     * @return A connection to the database.
     */
    private Connection createConnection() {
        Connection connection = null;
        try {
            // Establish a connection to the database
            connection = DriverManager.getConnection(DBURL, USER, PASS);
        } catch (SQLException e) {
            // Log and print the exception if connection fails
            LOGGER.log(Level.WARNING, "An error occurred while trying to connect to the database", e);
        }
        return connection;
    }

    /**
     * Retrieves a database connection instance.
     *
     * @return A connection to the database.
     */
    public static Connection getConnection() {
        return singleInstance.createConnection();
    }

    /**
     * Closes a database connection.
     *
     * @param connection The connection to be closed.
     */
    public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                // Log and print the exception if closing the connection fails
                LOGGER.log(Level.WARNING, "An error occurred while trying to close the connection", e);
            }
        }
    }

    /**
     * Closes a SQL statement.
     *
     * @param statement The SQL statement to be closed.
     */
    public static void close(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                // Log and print the exception if closing the statement fails
                LOGGER.log(Level.WARNING, "An error occurred while trying to close the statement", e);
            }
        }
    }

    /**
     * Closes a ResultSet.
     *
     * @param resultSet The ResultSet to be closed.
     */
    public static void close(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                // Log and print the exception if closing the ResultSet fails
                LOGGER.log(Level.WARNING, "An error occurred while trying to close the ResultSet", e);
            }
        }
    }
}
// Path: src/main/java/org/example/Dao/UserDAO.java
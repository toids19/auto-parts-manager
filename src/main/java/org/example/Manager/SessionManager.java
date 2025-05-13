/**
 * The SessionManager class manages the session state within the application.
 * It keeps track of the currently logged-in user and admin.
 */
package org.example.Manager;

import org.example.Model.User;

/**
 * Class to manage session state, specifically keeping track of the logged-in user and admin.
 */
public class SessionManager {

    /**
     * Static field to store the logged-in user.
     */
    public static User loggedInUser;

    /**
     * Static field to store the logged-in admin.
     */
    public static User loggedInAdmin;

    /**
     * Constructor for SessionManager. Initializes the logged-in user to null.
     */
    public SessionManager() {
        loggedInUser = null;
    }

    /**
     * Retrieves the currently logged-in admin.
     *
     * @return The currently logged-in admin.
     */
    public static User getLoggedInAdmin() {
        return loggedInAdmin;
    }

    /**
     * Retrieves the currently logged-in user.
     *
     * @return The currently logged-in user.
     */
    public static User getLoggedInUser() {
        return loggedInUser;
    }

    /**
     * Sets the logged-in user.
     *
     * @param user The user to be set as logged-in.
     */
    public static void setLoggedInUser(User user) {
        loggedInUser = user;
    }

    /**
     * Sets the logged-in admin.
     *
     * @param admin The admin to be set as logged-in.
     */
    public static void setLoggedInAdmin(User admin) {
        loggedInAdmin = admin;
    }
}

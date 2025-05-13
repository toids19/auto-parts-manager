/**
 * The FormsManager class manages the form transitions within the application.
 * It provides functionality to initialize the main application and to switch between different forms.
 */
package org.example.Manager;

import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import org.example.Main.Main;

import javax.swing.*;
import java.awt.*;

/**
 * Singleton class to manage form transitions within the application.
 */
public class FormsManager {

    /**
     * Singleton instance of FormsManager.
     */
    private static FormsManager instance;

    /**
     * Reference to the main application instance.
     */
    private static Main main;

    /**
     * Private constructor to prevent instantiation.
     */
    private FormsManager() {

    }

    /**
     * Retrieves the singleton instance of FormsManager.
     *
     * @return The singleton instance of FormsManager.
     */
    public static FormsManager getInstance() {
        if (instance == null) {
            instance = new FormsManager();
        }
        return instance;
    }

    /**
     * Initializes the FormsManager with the main application instance.
     *
     * @param main The main application instance.
     */
    public void initApplication(Main main) {
        FormsManager.main = main;
    }

    /**
     * Sets the view to the specified form.
     *
     * @param form The form to be displayed.
     */
    public static void showForm(JComponent form) {
        EventQueue.invokeLater(() -> {
            FlatAnimatedLafChange.showSnapshot();

            main.setContentPane(form);
            main.revalidate();
            main.repaint();

            FlatAnimatedLafChange.hideSnapshotWithAnimation();
        });
    }
}

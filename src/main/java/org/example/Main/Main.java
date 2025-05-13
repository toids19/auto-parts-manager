/**
 * The Main class serves as the entry point for the application.
 * It initializes and manages the main forms for user and admin dashboards.
 */
package org.example.Main;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import raven.toast.Notifications;
import org.example.View.*;
import org.example.View.dashboardAdmin.application.form.MainAdminForm;
import org.example.View.dashboardUser.application.form.MainUserForm;

import javax.swing.*;
import java.awt.*;

/**
 * The Main class extends JFrame and initializes the application GUI.
 */
@SuppressWarnings("ALL")
public class Main extends JFrame {

    /**
     * The main instance of the application.
     */
    private static Main app;

    /**
     * The main user form for user dashboard.
     */
    private static MainUserForm mainUserForm;

    /**
     * The main admin form for admin dashboard.
     */
    private static MainAdminForm mainAdminForm;

    /**
     * The login view for user authentication.
     */
    private static org.example.View.LoginView loginView;

    /**
     * Constructor for Main class.
     * Initializes the user and admin forms, and sets up the main window.
     */
    public Main() {
        mainUserForm = new MainUserForm();
        mainAdminForm = new MainAdminForm();
        loginView = new org.example.View.LoginView();

        initComponents();
        setSize(new Dimension(1300, 800));
        setLocationRelativeTo(null);
        setResizable(false);
        setContentPane(loginView);
        Notifications.getInstance().setJFrame(this);
    }

    /**
     * Initializes the GUI components.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 719, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 521, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Displays a given component in the user form.
     *
     * @param component The component to be displayed.
     */
    public static void showForm(Component component) {
        component.applyComponentOrientation(app.getComponentOrientation());
        app.mainUserForm.showForm(component);
    }

    /**
     * Displays a given component in the admin form.
     *
     * @param component The component to be displayed.
     */
    public static void showForm1(Component component) {
        component.applyComponentOrientation(app.getComponentOrientation());
        app.mainAdminForm.showForm(component);
    }

    /**
     * Sets the selected menu in the user form.
     *
     * @param index    The main menu index.
     * @param subIndex The submenu index.
     */
    public void setSelectedMenu(int index, int subIndex) {
        mainUserForm.setSelectedMenu(index, subIndex);
    }

    /**
     * The main method serves as the entry point for the application.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        FlatRobotoFont.install();
        FlatLaf.registerCustomDefaultsSource("/path/to/theme");
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
        FlatMacDarkLaf.setup();
        java.awt.EventQueue.invokeLater(() -> {
            app = new Main();
            app.setVisible(true);
        });
    }

    /**
     * Logs in a user and displays the user dashboard.
     */
    public static void loginUser() {
        FlatAnimatedLafChange.showSnapshot();
        app.setContentPane(mainUserForm);
        app.mainUserForm.applyComponentOrientation(app.getComponentOrientation());
        app.setSelectedMenu(0, 0);
        app.mainUserForm.hideMenu();
        SwingUtilities.updateComponentTreeUI(app.mainUserForm);
        FlatAnimatedLafChange.hideSnapshotWithAnimation();
    }

    /**
     * Logs in an admin and displays the admin dashboard.
     */
    public static void loginAdmin() {
        FlatAnimatedLafChange.showSnapshot();
        app.setContentPane(mainAdminForm);
        app.mainAdminForm.applyComponentOrientation(app.getComponentOrientation());
        app.setSelectedMenu(0, 0);
        app.mainAdminForm.hideMenu();
        SwingUtilities.updateComponentTreeUI(app.mainAdminForm);
        FlatAnimatedLafChange.hideSnapshotWithAnimation();
    }

    /**
     * Logs out the current user/admin and displays the login view.
     */
    public static void logout() {
        FlatAnimatedLafChange.showSnapshot();
        app.setContentPane(app.loginView);
        app.loginView.applyComponentOrientation(app.getComponentOrientation());
        SwingUtilities.updateComponentTreeUI(app.loginView);
        FlatAnimatedLafChange.hideSnapshotWithAnimation();
    }
}

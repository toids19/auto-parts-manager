/**
 * The LoginView class represents a JPanel for the login interface.
 * It provides input fields for username and password, a checkbox for remembering login,
 * and a button for initiating the login process.
 */

// Import statements
package org.example.View;

import com.formdev.flatlaf.FlatClientProperties;
import org.example.Controller.LoginController;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

@SuppressWarnings("ALL")
public class LoginView extends JPanel {

    // Controller for handling actions related to login
    private final LoginController loginController;

    // Text field for username input
    private JTextField txtUsername;

    // Password field for password input
    private JPasswordField txtPassword;

    // Checkbox for remembering login
    private JCheckBox chRememberMe;

    // Button for initiating the login process
    private JButton cmdLogin;

    /**
     * Constructs a LoginView object.
     * Initializes the LoginController associated with this view and sets up the UI components.
     */
    public LoginView() {
        loginController = new LoginController(this);
        init();
    }

    /**
     * Initializes the user interface.
     * Sets the layout, adds components for username, password, remember me checkbox, and login button.
     * Sets up styling properties and event listeners for UI components.
     */
    private void init() {
        setLayout(new MigLayout("fill,insets 10", "[right]40", "[center]"));

        // Add your logo on the left
        ImageIcon logoIcon = new ImageIcon("/Users/moldovanutudor/Desktop/PT/PT2024_30421_Moldovanu_Tudor/pt2024_30421_moldovanu_tudor_assignment_3/Pictures/logo_novocar.png");
        JLabel logoLabel = new JLabel(logoIcon);
        add(logoLabel, "spany, growy, gapleft 10"); // "spany" to cover the height of the panel, "growy" to make it vertically centered

        // Create the login panel
        txtUsername = new JTextField();
        txtPassword = new JPasswordField();
        chRememberMe = new JCheckBox("Ține-mă minte!");
        cmdLogin = new JButton("Loghează-te");
        JPanel panel = new JPanel(new MigLayout("wrap,fill,insets 35 45 30 45", "fill,250:280"));
        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:20;" +
                "[light]background:darken(@background,3%);" +
                "[dark]background:lighten(@background,3%);");
        txtPassword.putClientProperty(FlatClientProperties.STYLE, "" +
                "showRevealButton:true");

        cmdLogin.addActionListener(e -> loginController.login());

        cmdLogin.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]background:darken(@background,10%);" +
                "[dark]background:lighten(@background,10%);" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0");

        txtUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Introdu numele de utilizator");
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Introdu parola");
        JLabel lbTitle = new JLabel("Bine ai revenit!");
        lbTitle.setHorizontalAlignment(SwingConstants.CENTER); // Center the text within the label
        panel.add(lbTitle, "span, alignx center, grow");

        JLabel description = new JLabel("Loghează-te in contul tău de utilizator");
        description.setHorizontalAlignment(SwingConstants.CENTER); // Center the text within the label
        panel.add(description, "span, alignx center, grow");

        lbTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold +10");
        description.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]foreground:lighten(@foreground,30%);" +
                "[dark]foreground:darken(@foreground,30%)");

        panel.add(lbTitle);
        panel.add(description);
        panel.add(new JLabel("Utilizator"), "gapy 8");
        panel.add(txtUsername);
        panel.add(new JLabel("Parolă"), "gapy 8");
        panel.add(txtPassword);
        panel.add(chRememberMe, "grow 0");
        panel.add(cmdLogin, "gapy 10");

        add(panel);
    }

    // Getters and setters for UI components

    /**
     * Getter for the username text field.
     *
     * @return The JTextField object for username input.
     */
    public JTextField getTxtUsername() {
        return txtUsername;
    }

    /**
     * Setter for the username text field.
     *
     * @param txtUsername The JTextField object for username input.
     */
    public void setTxtUsername(JTextField txtUsername) {
        this.txtUsername = txtUsername;
    }

    /**
     * Getter for the password text field.
     *
     * @return The JPasswordField object for password input.
     */
    public JPasswordField getTxtPassword() {
        return txtPassword;
    }

    /**
     * Setter for the password text field.
     *
     * @param txtPassword The JPasswordField object for password input.
     */
    public void setTxtPassword(JPasswordField txtPassword) {
        this.txtPassword = txtPassword;
    }

    /**
     * Getter for the remember me checkbox.
     *
     * @return The JCheckBox object for remembering login.
     */
    public JCheckBox getChRememberMe() {
        return chRememberMe;
    }

    /**
     * Setter for the remember me checkbox.
     *
     * @param chRememberMe The JCheckBox object for remembering login.
     */
    public void setChRememberMe(JCheckBox chRememberMe) {
        this.chRememberMe = chRememberMe;
    }

    /**
     * Getter for the login button.
     *
     * @return The JButton object for initiating the login process.
     */
    public JButton getCmdLogin() {
        return cmdLogin;
    }

    /**
     * Setter for the login button.
     *
     * @param cmdLogin The JButton object for initiating the login process.
     */
    public void setCmdLogin(JButton cmdLogin) {
        this.cmdLogin = cmdLogin;
    }
}

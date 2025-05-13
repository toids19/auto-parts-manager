package org.example.View;

import javax.swing.*;
import com.formdev.flatlaf.FlatClientProperties;
import org.example.Controller.CreateUserController;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("ALL")
public class CreateUserView extends JPanel {

    public JTextField getTxtUserUsername() {
        return txtUserUsername;
    }

    public void setTxtUserUsername(JTextField txtUserUsername) {
        this.txtUserUsername = txtUserUsername;
    }

    public JPasswordField getTxtUserPassword() {
        return txtUserPassword;
    }

    public void setTxtUserPassword(JPasswordField txtUserPassword) {
        this.txtUserPassword = txtUserPassword;
    }

    private final CreateUserController createUserController;

    private JTextField txtUserUsername;
    private JPasswordField txtUserPassword;

    // Add fields for other user attributes
    private JTextField txtFirstName;
    private JTextField txtLastName;
    private JTextField txtAddress;
    private JTextField txtEmail;
    private JTextField txtPhoneNumber;
    private JTextField txtRole;


    private void init() {
        setLayout(new MigLayout("fill, insets 20", "[center]", "[center]"));

        txtUserUsername = new JTextField();
        txtUserPassword = new JPasswordField();

        // Initialize other text fields
        txtFirstName = new JTextField();
        txtLastName = new JTextField();
        txtAddress = new JTextField();
        txtEmail = new JTextField();
        txtPhoneNumber = new JTextField();
        txtRole = new JTextField();

        cmdFinal = new JButton("Finalizare");
        cmdFinal.addActionListener(e -> createUserController.finalAction());

        JPanel panel = new JPanel(new MigLayout("wrap, fillx, insets 35 45 30 45", "[fill,360]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:20;" +
                "[light]background:darken(@background,3%);" +
                "[dark]background:lighten(@background,3%)");

        txtUserUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Introdu Username-ul noului utilizator");
        txtUserPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Introdu Parola noului utilizator");
        txtFirstName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Introdu Prenumele noului utilizator");
        txtLastName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Introdu Numele noului utilizator");
        txtAddress.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Introdu Adresa noului utilizator");
        txtEmail.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Introdu Email-ul noului utilizator");
        txtPhoneNumber.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Introdu Numărul de Telefon al noului utilizator");
        txtRole.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Introdu rolul noului utilizator");

        cmdFinal.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]background:darken(@background,10%);" +
                "[dark]background:lighten(@background,10%);" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0");

        JLabel lbTitle = new JLabel("               Creare Cont User");
        JLabel description = new JLabel("             Completați informațiile de logare ale noului User");

        lbTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold +10");
        description.putClientProperty(FlatClientProperties.STYLE, "[light]foreground:lighten(@foreground,30%);[dark]foreground:darken(@foreground,30%)");

        panel.add(lbTitle, "align center");
        panel.add(description, "align center, wrap");
        panel.add(new JLabel("Nume Utilizator Nou"), "gapy 10");
        panel.add(txtUserUsername, "wrap");
        panel.add(new JLabel("Parolă Utilizator Nou"), "gapy 10");
        panel.add(txtUserPassword, "wrap");

        // Add labels and text fields for other user attributes
        panel.add(new JLabel("Prenume Utilizator Nou"), "gapy 10");
        panel.add(txtFirstName, "wrap");
        panel.add(new JLabel("Nume Utilizator Nou"), "gapy 10");
        panel.add(txtLastName, "wrap");
        panel.add(new JLabel("Adresă Utilizator Nou"), "gapy 10");
        panel.add(txtAddress, "wrap");
        panel.add(new JLabel("Email Utilizator Nou"), "gapy 10");
        panel.add(txtEmail, "wrap");
        panel.add(new JLabel("Număr de Telefon Utilizator Nou"), "gapy 10");
        panel.add(txtPhoneNumber, "wrap");
        panel.add(new JLabel("Rol Utilizator Nou"), "gapy 10");
        panel.add(txtRole, "wrap");

        panel.add(cmdFinal, " gapy 20");

        add(panel);
    }

    public JButton getCmdFinal() {
        return cmdFinal;
    }

    public void setCmdFinal(JButton cmdFinal) {
        this.cmdFinal = cmdFinal;
    }

    private JButton cmdFinal;

    public CreateUserView() {
        createUserController = new CreateUserController(this);
        init();
    }

    public CreateUserController getCreateUserController() {
        return createUserController;
    }

    public JTextField getTxtFirstName() {
        return txtFirstName;
    }

    public void setTxtFirstName(JTextField txtFirstName) {
        this.txtFirstName = txtFirstName;
    }

    public JTextField getTxtLastName() {
        return txtLastName;
    }

    public void setTxtLastName(JTextField txtLastName) {
        this.txtLastName = txtLastName;
    }

    public JTextField getTxtAddress() {
        return txtAddress;
    }

    public void setTxtAddress(JTextField txtAddress) {
        this.txtAddress = txtAddress;
    }

    public JTextField getTxtEmail() {
        return txtEmail;
    }

    public void setTxtEmail(JTextField txtEmail) {
        this.txtEmail = txtEmail;
    }

    public JTextField getTxtPhoneNumber() {
        return txtPhoneNumber;
    }

    public void setTxtPhoneNumber(JTextField txtPhoneNumber) {
        this.txtPhoneNumber = txtPhoneNumber;
    }

    public JTextField getTxtRole() {
        return txtRole;
    }

    public void setTxtRole(JTextField txtRole) {
        this.txtRole = txtRole;
    }



}

/**
 * The CreateProductView class represents a JPanel for creating new products.
 * It provides input fields for product details and a button for finalizing product creation.
 */

// Import statements
package org.example.View;

import javax.swing.*;
import com.formdev.flatlaf.FlatClientProperties; // FlatLaf library for UI styling
import org.example.Controller.CreateProductController;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("ALL") // Suppressing warnings - Avoid suppressing all warnings, use specific ones if needed.
// CreateProductView class
public class CreateProductView extends JPanel {

    // Controller for handling actions related to creating products
    private final CreateProductController createProductController;

    public JTextField getTxtProductDescription() {
        return txtProductDescription;
    }

    public void setTxtProductDescription(JTextField txtProductDescription) {
        this.txtProductDescription = txtProductDescription;
    }

    public JTextField getTxtProductPrice() {
        return txtProductPrice;
    }

    public void setTxtProductPrice(JTextField txtProductPrice) {
        this.txtProductPrice = txtProductPrice;
    }

    public JTextField getTxtProductStock() {
        return txtProductStock;
    }

    public void setTxtProductStock(JTextField txtProductStock) {
        this.txtProductStock = txtProductStock;
    }

    public JButton getCmdFinal() {
        return cmdFinal;
    }

    public void setCmdFinal(JButton cmdFinal) {
        this.cmdFinal = cmdFinal;
    }

    // Text fields and button for input and actions
    private JTextField txtProductName;
    private JTextField txtProductDescription;
    private JTextField txtProductPrice;
    private JTextField txtProductStock;
    private JButton cmdFinal;

    // Getters and setters for text fields and button
    public JTextField getTxtProductName() {
        return txtProductName;
    }

    public void setTxtProductName(JTextField txtProductName) {
        this.txtProductName = txtProductName;
    }

    // (Similar getter and setter methods for other text fields and button)

    // Initialize method for setting up the UI
    private void init() {
        // Set layout using MigLayout with horizontal and vertical fill, and insets
        setLayout(new MigLayout("fill, insets 20", "[center]", "[center]"));

        // Initialize text fields and button
        txtProductName = new JTextField();
        txtProductDescription = new JTextField();
        txtProductPrice = new JTextField();
        txtProductStock = new JTextField();
        cmdFinal = new JButton("Finalizare");

        // Add action listener to the "Finalizare" button
        cmdFinal.addActionListener(e -> createProductController.finalAction());

        // Create panel for organizing components with MigLayout
        JPanel panel = new JPanel(new MigLayout("wrap, fillx, insets 35 45 30 45", "[fill,360]"));

        // Set custom styling properties for the panel
        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:20;" +
                "[light]background:darken(@background,3%);" +
                "[dark]background:lighten(@background,3%)");

        // Set placeholder text for text fields
        txtProductName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Introdu Numele noului produs");
        txtProductDescription.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Introdu Descrierea noului produs");
        txtProductPrice.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Introdu Prețul noului produs");
        txtProductStock.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Introdu Stocul noului produs");
        // Set custom styling properties for the "Finalizare" button
        cmdFinal.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]background:darken(@background,10%);" +
                "[dark]background:lighten(@background,10%);" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0");

        // Create labels for title and description
        JLabel lbTitle = new JLabel("               Creare Produs Nou");
        JLabel description = new JLabel("                           Completați informațiile noului produs");

        // Set styling properties for the title and description labels
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold +10");
        description.putClientProperty(FlatClientProperties.STYLE, "[light]foreground:lighten(@foreground,30%);[dark]foreground:darken(@foreground,30%)");

        // Add components to the panel using MigLayout constraints
        panel.add(lbTitle, "align center");
        panel.add(description, "align center, wrap");
        panel.add(new JLabel("Nume Produs Nou"), "gapy 10");
        panel.add(txtProductName, "wrap");
        panel.add(new JLabel("Descriere Produs Nou"), "gapy 10");
        panel.add(txtProductDescription, "wrap");
        panel.add(new JLabel("Preț Produs Nou"), "gapy 10");
        panel.add(txtProductPrice, "wrap");
        panel.add(new JLabel("Stoc Produs Nou"), "gapy 10");
        panel.add(txtProductStock, "wrap");
        panel.add(cmdFinal, " gapy 20");

        // Add the panel to the main panel
        add(panel);
    }

    // Constructor
    public CreateProductView() {
        // Initialize the controller for this view
        createProductController = new CreateProductController(this);
        // Initialize the UI
        init();
    }

    // Getter for the CreateProductController
    public CreateProductController getCreateProductController() {
        return createProductController;
    }

    // Getters and setters for other text fields and button
}

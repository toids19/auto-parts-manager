/**
 * The ProductListView class represents a JPanel for displaying a list of products.
 * It includes a table to display product details and buttons for editing, deleting, and refreshing the product list.
 */

// Import statements
package org.example.View;

import org.example.Connection.ConnectionFactory;
import org.example.Model.Product;
import org.example.Repository.ProductRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

@SuppressWarnings("ALL")
public class ProductListView extends JPanel {

    // Table model for displaying product data
    private DefaultTableModel tableModel;

    // Table for displaying product data
    private JTable productTable;

    // Repository for accessing product data
    private final ProductRepository productRepository;

    /**
     * Constructs a ProductListView object.
     * Initializes the ProductRepository associated with this view and sets up the UI components.
     */
    public ProductListView() {
        productRepository = new ProductRepository(ConnectionFactory.getConnection());
        initUI();
    }

    /**
     * Initializes the user interface.
     * Sets the layout, creates table model, populates the table with product data, and adds buttons for interaction.
     */
    private void initUI() {
        setLayout(new BorderLayout());

        // Create a table model
        tableModel = new DefaultTableModel();

        // Get all products from the ProductRepository and populate the table
        refreshProductsTable();

        // Create the JTable with the table model
        productTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };

        // Set the preferred size of the table
        productTable.setPreferredScrollableViewportSize(new Dimension(600, 200));

        JScrollPane scrollPane = new JScrollPane(productTable);

        // Add the scroll pane to the panel
        add(scrollPane, BorderLayout.CENTER);

        // Add buttons at the bottom
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS)); // Horizontal layout

        // Create the "Edit" button
        JButton editButton = new JButton("EDITEAZĂ");
        editButton.addActionListener(e -> editProduct());

        // Create the "Delete" button
        JButton deleteButton = new JButton("ȘTERGE");
        deleteButton.addActionListener(e -> deleteSelectedProduct());

        // Create the "Refresh" button
        JButton refreshButton = new JButton("REFRESH");
        refreshButton.addActionListener(e -> refreshProductsTable());

        // Add buttons to the button panel
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(Box.createHorizontalGlue()); // Add spacing between buttons
        buttonPanel.add(refreshButton);

        // Add the button panel to the bottom of the panel
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Refreshes the product table by clearing and repopulating it with data from the ProductRepository.
     */
    private void refreshProductsTable() {
        // Clear the table model
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);

        // Get all products from the ProductRepository
        List<Product> products = productRepository.getAllProducts();

        if (!products.isEmpty()) {
            // Use reflection to get the column names
            Class<?> productClass = products.get(0).getClass();
            for (java.lang.reflect.Field field : productClass.getDeclaredFields()) {
                tableModel.addColumn(field.getName());
            }

            // Populate the table with product details
            for (Product product : products) {
                Object[] rowData = new Object[productClass.getDeclaredFields().length];
                int i = 0;
                for (java.lang.reflect.Field field : productClass.getDeclaredFields()) {
                    field.setAccessible(true);
                    try {
                        rowData[i] = field.get(product);
                    } catch (IllegalAccessException ex) {
                        ex.printStackTrace();
                    }
                    i++;
                }
                tableModel.addRow(rowData);
            }
        }
    }

    /**
     * Opens a dialog for editing the selected product.
     */
    private void editProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow != -1) {
            // Get the Product object corresponding to the selected row
            Product product = getProductFromSelectedRow(selectedRow);
            // Open edit product dialog
            EditProductDialog editProductDialog = new EditProductDialog(product);
            editProductDialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Te rog selectează un produs pentru a fi editat.");
        }
    }

    /**
     * Deletes the selected product after confirming with the user.
     */
    private void deleteSelectedProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow != -1) {
            // Get the Product object corresponding to the selected row
            Product product = getProductFromSelectedRow(selectedRow);
            int option = JOptionPane.showConfirmDialog(this, "Ești sigur ca vrei să ștergi produsul: " + product.getProductName() + "?", "Confirmă Ștergerea", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                // Delete the product using ProductRepository
                productRepository.deleteByProductName(product.getProductName());
                // Refresh the table
                refreshProductsTable();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Te rog selectează un produs pentru a fi șters.");
        }
    }

    /**
     * Retrieves the Product object corresponding to the selected row in the table.
     *
     * @param selectedRow The index of the selected row.
     * @return The Product object corresponding to the selected row.
     */
    private Product getProductFromSelectedRow(int selectedRow) {
        // Get the Product object corresponding to the selected row
        Product product = new Product();
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            String columnName = tableModel.getColumnName(i);
            Object columnValue = tableModel.getValueAt(selectedRow, i);
            try {
                java.lang.reflect.Field field = Product.class.getDeclaredField(columnName);
                field.setAccessible(true);
                if (field.getType().equals(Long.class)) {
                    field.set(product, Long.parseLong(columnValue.toString()));
                } else {
                    field.set(product, columnValue);
                }
            } catch (NoSuchFieldException | IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }
        return product;
    }

    /**
     * The EditProductDialog class represents a dialog for editing product details.
     */
    private static class EditProductDialog extends JDialog {
        // Text fields for editing product details
        private final JTextField productNameField;
        private final JTextField productDescriptionField;
        private final JTextField productPriceField;
        private final JTextField productStockField;

        // Product object to be edited
        private final Product product;

        /**
         * Constructs an EditProductDialog object.
         *
         * @param product The Product object to be edited.
         */
        public EditProductDialog(Product product) {
            this.product = product;
            setTitle("Edit Product");
            setSize(350, 300); // Adjust size as needed
            setLayout(new GridLayout(5, 2)); // Adjust rows and columns as needed

            // Initialize text fields with product details
            productNameField = new JTextField(product.getProductName());
            productDescriptionField = new JTextField(product.getProductDescription());
            productPriceField = new JTextField(String.valueOf(product.getProductPrice()));
            productStockField = new JTextField(String.valueOf(product.getProductStock()));

            // Make the productName field not editable
            productNameField.setEditable(false);

            // Add text fields to the dialog
            add(new JLabel("Product Name:"));
            add(productNameField);
            add(new JLabel("Description:"));
            add(productDescriptionField);
            add(new JLabel("Price:"));
            add(productPriceField);
            add(new JLabel("Stock:"));
            add(productStockField);

            // Add "Save" button
            JButton saveButton = new JButton("Save");
            saveButton.addActionListener(e -> saveChanges());
            add(saveButton, "warp center");

            // Center the dialog on the screen
            setLocationRelativeTo(null);
            setResizable(false);
        }

        /**
         * Saves the changes made to the product details.
         */
        private void saveChanges() {
            // Update product details
            product.setProductDescription(productDescriptionField.getText());
            product.setProductPrice(Double.parseDouble(productPriceField.getText()));
            product.setProductStock(Integer.parseInt(productStockField.getText()));

            // Update product in the database using ProductRepository
            ProductRepository productRepository = new ProductRepository(ConnectionFactory.getConnection());
            productRepository.updateByProductName(product.getProductName(), product);

            // Close the dialog
            dispose();
        }
    }
}

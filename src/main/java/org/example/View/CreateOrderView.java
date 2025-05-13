package org.example.View;

import org.example.Connection.ConnectionFactory;
import org.example.Manager.SessionManager;
import org.example.Model.Bill;
import org.example.Model.Product;
import org.example.Model.User;
import org.example.Repository.BillRepository;
import org.example.Repository.OrderRepository;
import org.example.Repository.ProductRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Timestamp;
import java.util.List;

public class CreateOrderView extends JPanel {

    private DefaultTableModel tableModel;
    private JTable productTable;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public CreateOrderView() {
        productRepository = new ProductRepository(ConnectionFactory.getConnection());
        orderRepository = new OrderRepository();
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel();

        refreshProductsTable();

        productTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        productTable.setPreferredScrollableViewportSize(new Dimension(600, 200));

        JScrollPane scrollPane = new JScrollPane(productTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));

        JButton orderButton = new JButton("COMANDĂ");
        orderButton.addActionListener(e -> placeOrder());

        JButton refreshButton = new JButton("REFRESH");
        refreshButton.addActionListener(e -> refreshProductsTable());

        buttonPanel.add(orderButton);
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(refreshButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void refreshProductsTable() {
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);

        List<Product> products = productRepository.getAllProducts();

        if (!products.isEmpty()) {
            Class<?> productClass = products.get(0).getClass();
            for (java.lang.reflect.Field field : productClass.getDeclaredFields()) {
                tableModel.addColumn(field.getName());
            }

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

    private void placeOrder() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow != -1) {
            Product product = getProductFromSelectedRow(selectedRow);
            displayProductDetails(product);
            int quantity = getQuantityFromUser(product);
            if (quantity > 0 && product.getProductStock() >= quantity) {
                double totalPrice = quantity * product.getProductPrice();

                // Place order and get the order ID
                Long orderId = orderRepository.placeOrder(product.getProductId(), quantity, totalPrice);

                if (orderId != null) {
                    // Generate bill
                    generateBill(orderId, product, quantity, totalPrice);

                    // Display order summary and options for closing or generating invoice
                    displayOrderSummary(product, quantity, totalPrice);
                    refreshProductsTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Eroare la plasarea comenzii!", "Eroare", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Stoc insuficient!", "Eroare", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vă rugăm să selectați un produs.", "Eroare", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generateBill(Long orderId, Product product, int quantity, double totalPrice) {
        // Create a new bill using the provided parameters
        Bill bill = new Bill(orderId, product.getProductName(), quantity, totalPrice,
                SessionManager.getLoggedInUser().getUsername(),
                SessionManager.getLoggedInUser().getFirstName(),
                SessionManager.getLoggedInUser().getLastName(),
                SessionManager.getLoggedInUser().getAddress(),
                SessionManager.getLoggedInUser().getPhoneNumber(),
                new Timestamp(System.currentTimeMillis()));

        // Insert the bill into the database
        BillRepository billRepository = new BillRepository();
        // Declare the variable outside the method
        Bill insertedBill = billRepository.insertBill(bill);

    }



    private void displayProductDetails(Product product) {
        String message = getProductDetailsMessage(product);
        JOptionPane.showMessageDialog(this, message, "Detalii produs", JOptionPane.INFORMATION_MESSAGE);
    }

    private int getQuantityFromUser(Product product) {
        int quantity = -1;
        boolean validInput = false;
        while (!validInput) {
            String input = JOptionPane.showInputDialog(this, "Introduceți cantitatea (disponibil: " + product.getProductStock() + "):", "Introducere Cantitate", JOptionPane.PLAIN_MESSAGE);
            try {
                quantity = Integer.parseInt(input);
                if (quantity > 0 && quantity <= product.getProductStock()) {
                    validInput = true;
                } else {
                    JOptionPane.showMessageDialog(this, "Cantitatea trebuie să fie un număr natural mai mare decât 0 și mai mic sau egal cu stocul disponibil.", "Eroare", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Cantitate invalidă. Vă rugăm să introduceți un număr valid.", "Eroare", JOptionPane.ERROR_MESSAGE);
            }
        }
        return quantity;
    }

    private Product getProductFromSelectedRow(int selectedRow) {
        Product product = new Product();
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            String columnName = tableModel.getColumnName(i);
            Object columnValue = tableModel.getValueAt(selectedRow, i);
            try {
                java.lang.reflect.Field field = Product.class.getDeclaredField(columnName);
                field.setAccessible(true);
                if (field.getType().equals(Long.class)) {
                    field.set(product, Long.parseLong(columnValue.toString()));
                } else if (field.getType().equals(Double.class)) {
                    field.set(product, Double.parseDouble(columnValue.toString()));
                } else if (field.getType().equals(Integer.class)) {
                    field.set(product, Integer.parseInt(columnValue.toString()));
                } else {
                    field.set(product, columnValue);
                }
            } catch (NoSuchFieldException | IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }
        return product;
    }

    private void displayOrderSummary(Product product, int quantity, double totalPrice) {
        String message = getProductDetailsMessage(product) +
                "Cantitate comandată: " + quantity + "\n" +
                "Preț total: " + totalPrice + "\n";
        Object[] options = {"INCHIDE"};
        JOptionPane.showOptionDialog(this, message, "Comandă plasată cu succes!", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
    }


    private String getProductDetailsMessage(Product product) {
        String message = "Numele produsului: " + product.getProductName() + "\n" +
                "Descriere: " + product.getProductDescription() + "\n" +
                "Preț: " + product.getProductPrice() + "\n" +
                "Stoc disponibil: " + product.getProductStock() + "\n";
        return message;
    }

    private void generateInvoice(Bill bill) {
        // Create an instance of BillListView
        BillListView billListView = new BillListView();

        // Call the method on the instance
        billListView.generatePDFInvoice(bill);

        JOptionPane.showMessageDialog(this, "Factura generată cu succes!", "Succes", JOptionPane.INFORMATION_MESSAGE);
    }

}

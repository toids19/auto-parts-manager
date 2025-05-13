/**
 * The CreateProductController class is responsible for managing the creation of new products.
 * It handles input validation, interaction with the ProductRepository, and user notifications.
 */
package org.example.Controller;

import org.example.Connection.ConnectionFactory;
import org.example.View.CreateProductView;
import org.example.Repository.ProductRepository;
import org.example.Model.Product;
import raven.toast.Notifications;
import org.example.View.dashboardUser.application.form.other.DefaultForm;
import org.example.Main.Main;

import java.sql.SQLException;
import java.util.regex.Pattern;

/**
 * The CreateProductController class handles the creation of products.
 */
public class CreateProductController {

    /**
     * The view for creating a product.
     */
    private final CreateProductView createProductView;

    /**
     * The repository for interacting with the product database.
     */
    private final ProductRepository productRepository;

    /**
     * Constructor for CreateProductController.
     *
     * @param createProductView The view associated with this controller.
     */
    public CreateProductController(CreateProductView createProductView) {
        this.createProductView = createProductView;
        this.productRepository = new ProductRepository(ConnectionFactory.getConnection());
    }

    /**
     * Final action to be performed when creating a product.
     * Validates the input and creates a new product if validation is successful.
     */
    public void finalAction() {
        if (checkInput()) {
            Product newProduct = new Product();
            newProduct.setProductName(createProductView.getTxtProductName().getText().trim());
            newProduct.setProductDescription(createProductView.getTxtProductDescription().getText().trim());
            newProduct.setProductPrice(Double.parseDouble(createProductView.getTxtProductPrice().getText().trim()));
            newProduct.setProductStock(Integer.parseInt(createProductView.getTxtProductStock().getText().trim()));

            productRepository.createProduct(newProduct);

            Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.TOP_CENTER, "Produsul a fost creat cu succes!");

            // Optionally, navigate to the next view or perform any other action
            Main.showForm1(new DefaultForm(""));
        }
    }

    /**
     * Checks the input fields for creating a product.
     *
     * @return true if all input fields are valid, false otherwise.
     */
    private boolean checkInput() {
        String productName = createProductView.getTxtProductName().getText().trim();
        if (productName.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER, "Câmpul 'Numele Produsului' nu poate fi gol.");
            return false;
        }

        // Check if product name already exists
        if (productRepository.existsByProductName(productName)) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER, "Există deja un produs cu acest nume.");
            return false;
        }

        String productDescription = createProductView.getTxtProductDescription().getText().trim();
        if (productDescription.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER, "Câmpul 'Descrierea Produsului' nu poate fi gol.");
            return false;
        }

        String productPrice = createProductView.getTxtProductPrice().getText().trim();
        if (productPrice.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER, "Câmpul 'Preț' nu poate fi gol.");
            return false;
        }

        try {
            double price = Double.parseDouble(productPrice);
            if (price <= 0) {
                Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER, "Prețul trebuie să fie mai mare decât 0.");
                return false;
            }
        } catch (NumberFormatException e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER, "Câmpul 'Preț' trebuie să fie un număr valid.");
            return false;
        }

        String productStock = createProductView.getTxtProductStock().getText().trim();
        if (productStock.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER, "Câmpul 'Stoc' nu poate fi gol.");
            return false;
        }

        try {
            int stock = Integer.parseInt(productStock);
            if (stock < 0) {
                Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER, "Stocul trebuie să fie mai mare sau egal cu 0.");
                return false;
            }
        } catch (NumberFormatException e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER, "Câmpul 'Stoc' trebuie să fie un număr valid.");
            return false;
        }

        return true;
    }
}

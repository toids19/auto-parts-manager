/**
 * The CreateUserController class is responsible for managing the creation of new users.
 * It handles input validation, interaction with the UserRepository, and user notifications.
 */
package org.example.Controller;

import org.example.Connection.ConnectionFactory;
import org.example.View.CreateUserView;
import org.example.Repository.UserRepository;
import org.example.Model.User;
import raven.toast.Notifications;
import org.example.View.dashboardUser.application.form.other.DefaultForm;
import org.example.Main.Main;

import java.sql.SQLException;
import java.util.regex.Pattern;

/**
 * The CreateUserController class handles the creation of users.
 */
public class CreateUserController {

    /**
     * The view for creating a user.
     */
    private final CreateUserView createUserView;

    /**
     * The repository for interacting with the user database.
     */
    private final UserRepository userRepository;

    /**
     * Constructor for CreateUserController.
     *
     * @param createUserView The view associated with this controller.
     */
    public CreateUserController(CreateUserView createUserView) {
        this.createUserView = createUserView;
        this.userRepository = new UserRepository(ConnectionFactory.getConnection());
    }

    /**
     * Final action to be performed when creating a user.
     * Validates the input and creates a new user if validation is successful.
     */
    public void finalAction() {
        if (checkInput()) {
            User newUser = new User();
            newUser.setUsername(createUserView.getTxtUserUsername().getText().trim());
            newUser.setPassword(createUserView.getTxtUserPassword().getText().trim());
            newUser.setFirstName(createUserView.getTxtFirstName().getText().trim());
            newUser.setLastName(createUserView.getTxtLastName().getText().trim());
            newUser.setAddress(createUserView.getTxtAddress().getText().trim());
            newUser.setEmail(createUserView.getTxtEmail().getText().trim());
            newUser.setPhoneNumber(createUserView.getTxtPhoneNumber().getText().trim());
            newUser.setRole(createUserView.getTxtRole().getText().trim());

            userRepository.createUser(newUser);

            Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.TOP_CENTER, "Utilizatorul a fost creat cu succes!");

            // Optionally, navigate to the next view or perform any other action
            Main.showForm1(new DefaultForm(""));
        }
    }

    /**
     * Checks the input fields for creating a user.
     *
     * @return true if all input fields are valid, false otherwise.
     */
    private boolean checkInput() {
        String username = createUserView.getTxtUserUsername().getText().trim();
        if (username.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER, "Câmpul 'Numele de Utilizator' nu poate fi gol.");
            return false;
        }

        // Check if username already exists
        if (userRepository.existsByUsername(username)) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER, "Există deja un utilizator cu acest nume de utilizator.");
            return false;
        }

        String password = createUserView.getTxtUserPassword().getText().trim();
        if (password.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER, "Câmpul 'Parolă' nu poate fi gol.");
            return false;
        }

        String firstName = createUserView.getTxtFirstName().getText().trim();
        if (firstName.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER, "Câmpul 'Prenume' nu poate fi gol.");
            return false;
        }

        String lastName = createUserView.getTxtLastName().getText().trim();
        if (lastName.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER, "Câmpul 'Nume' nu poate fi gol.");
            return false;
        }

        String address = createUserView.getTxtAddress().getText().trim();
        if (address.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER, "Câmpul 'Adresă' nu poate fi gol.");
            return false;
        }

        String email = createUserView.getTxtEmail().getText().trim();
        if (email.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER, "Câmpul 'Email' nu poate fi gol.");
            return false;
        }

        if (!isValidEmail(email)) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER, "Câmpul 'Email' nu este valid.");
            return false;
        }

        String phoneNumber = createUserView.getTxtPhoneNumber().getText().trim();
        if (phoneNumber.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER, "Câmpul 'Număr de Telefon' nu poate fi gol.");
            return false;
        }

        if (!isValidPhoneNumber(phoneNumber)) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER, "Câmpul 'Număr de Telefon' nu este valid.");
            return false;
        }

        String role = createUserView.getTxtRole().getText().trim();
        if (role.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER, "Câmpul 'Rol' nu poate fi gol.");
            return false;
        }

        // Check if role is either "CLIENT" or "ADMINISTRATOR"
        if (!role.equals("CLIENT") && !role.equals("ADMINISTRATOR")) {
            Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER, "Rolul trebuie să fie 'CLIENT' sau 'ADMINISTRATOR'.");
            return false;
        }

        // Additional validation checks can be added here

        return true;
    }

    /**
     * Checks if an email address is valid.
     *
     * @param email The email address to be checked.
     * @return true if the email address is valid, false otherwise.
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    /**
     * Checks if a phone number is valid.
     *
     * @param phoneNumber The phone number to be checked.
     * @return true if the phone number is valid, false otherwise.
     */
    private boolean isValidPhoneNumber(String phoneNumber) {
        String phoneNumberRegex = "^[+]?[0-9]{10,13}$";
        Pattern pattern = Pattern.compile(phoneNumberRegex);
        return pattern.matcher(phoneNumber).matches();
    }
}

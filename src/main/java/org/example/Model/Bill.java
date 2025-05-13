/**
 * The Bill class represents a bill entity in the application.
 * It contains information about an order, including product details, user information, and creation timestamp.
 */
package org.example.Model;

import java.sql.Timestamp;

/**
 * A record representing a bill entity.
 */
public record Bill(
        Long orderId, // ID of the order
        String productName, // Name of the product
        int quantity, // Quantity of the product in the order
        double totalPrice, // Total price of the order
        String username, // Username of the user who made the order
        String firstName, // First name of the user
        String lastName, // Last name of the user
        String address, // Address of the user
        String phoneNumber, // Phone number of the user
        Timestamp createdAt // Timestamp of when the bill was created
) {

    /**
     * Provides a string representation of the record.
     *
     * @return A string representation of the record.
     */
    @Override
    public String toString() {
        return "Bill{" +
                "orderId=" + orderId +
                ", productName='" + productName + '\'' +
                ", quantity=" + quantity +
                ", totalPrice=" + totalPrice +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    /**
     * Retrieves the ID of the order.
     *
     * @return The ID of the order.
     */
    public Long getOrderId() {
        return orderId;
    }

    /**
     * Retrieves the name of the product.
     *
     * @return The name of the product.
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Retrieves the quantity of the product in the order.
     *
     * @return The quantity of the product in the order.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Retrieves the total price of the order.
     *
     * @return The total price of the order.
     */
    public double getTotalPrice() {
        return totalPrice;
    }

    /**
     * Retrieves the username of the user who made the order.
     *
     * @return The username of the user who made the order.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Retrieves the first name of the user.
     *
     * @return The first name of the user.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Retrieves the last name of the user.
     *
     * @return The last name of the user.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Retrieves the address of the user.
     *
     * @return The address of the user.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Retrieves the phone number of the user.
     *
     * @return The phone number of the user.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Retrieves the timestamp of when the bill was created.
     *
     * @return The timestamp of when the bill was created.
     */
    public Timestamp getCreatedAt() {
        return createdAt;
    }
}

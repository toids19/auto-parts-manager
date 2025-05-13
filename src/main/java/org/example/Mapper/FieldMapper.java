/**
 * The FieldMapper class maps field names to column names for database tables.
 * It provides mappings for various tables such as USERS, ORDERS, PRODUCTS, and BILLS.
 */
package org.example.Mapper;

import java.util.HashMap;
import java.util.Map;

/**
 * Maps field names to column names for database tables.
 */
@SuppressWarnings("ALL")
public class FieldMapper {

    /**
     * A map to store field-to-column mappings for database tables.
     */
    private static final Map<String, String> fieldToColumnMap = new HashMap<>();

    /**
     * Static initialization block to populate the field-to-column mappings.
     */
    static {
        // Mappings for USERS table
        fieldToColumnMap.put("userId", "user_id");
        fieldToColumnMap.put("username", "username");
        fieldToColumnMap.put("password", "password");
        fieldToColumnMap.put("firstName", "first_name");
        fieldToColumnMap.put("lastName", "last_name");
        fieldToColumnMap.put("address", "address");
        fieldToColumnMap.put("email", "email");
        fieldToColumnMap.put("phoneNumber", "phone_number");
        fieldToColumnMap.put("role", "role");

        // Mappings for ORDERS table
        fieldToColumnMap.put("orderId", "order_id");
        fieldToColumnMap.put("userId", "user_id");
        fieldToColumnMap.put("productId", "product_id");
        fieldToColumnMap.put("quantity", "quantity");
        fieldToColumnMap.put("totalPrice", "total_price");
        fieldToColumnMap.put("createdAt", "created_at");

        // Mappings for PRODUCTS table
        fieldToColumnMap.put("productId", "product_id");
        fieldToColumnMap.put("productName", "product_name");
        fieldToColumnMap.put("productDescription", "product_description");
        fieldToColumnMap.put("productPrice", "product_price");
        fieldToColumnMap.put("productStock", "product_stock");

        // Mappings for BILLS table
        fieldToColumnMap.put("orderId", "order_id");
        fieldToColumnMap.put("productName", "product_name");
        fieldToColumnMap.put("quantity", "quantity");
        fieldToColumnMap.put("totalPrice", "total_price");
        fieldToColumnMap.put("username", "username");
        fieldToColumnMap.put("firstName", "first_name");
        fieldToColumnMap.put("lastName", "last_name");
        fieldToColumnMap.put("address", "address");
        fieldToColumnMap.put("phoneNumber", "phone_number");
        fieldToColumnMap.put("createdAt", "created_at");
    }

    /**
     * Maps a field name to its corresponding column name in the database table.
     *
     * @param fieldName The name of the field.
     * @return The corresponding column name in the database table.
     */
    public static String mapFieldNameToColumnName(String fieldName) {
        return fieldToColumnMap.getOrDefault(fieldName, fieldName.toLowerCase());
    }
}

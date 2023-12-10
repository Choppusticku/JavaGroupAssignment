/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filehandling;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import model.Customer;
import model.MenuItem;
import model.Order;
import model.OrderStatus;
import model.Vendor;

public class OrderDatabase {

    static String filePath = "orders.txt";

    public static void addOrderToTxt(Order order) {
        try ( FileWriter fileWriter = new FileWriter(filePath, true);  BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

            // Compose a comma-separated string of order attributes
            String orderString = String.format("%s,%s,%s,%s,%s,%b,%s",
                    order.getOrderId(),
                    order.getCustomer().getUsername(),
                    order.getVendor().getUsername(),
                    order.getItemString(),
                    order.getStatus(),
                    order.isDeliveryRequested(),
                    order.getTotal());

            // Write the order string to the file
            bufferedWriter.write(orderString);
            bufferedWriter.newLine(); // Move to the next line for the next order
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }
    }

    public static ArrayList<Order> getAllOrders() {
        ArrayList<Order> vendorOrders = new ArrayList<>();

        try ( FileReader fileReader = new FileReader(filePath);  BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] orderAttributes = line.split(",");

                // Extract relevant attributes for creating the Order
                String orderId = orderAttributes[0];
                String customerId = orderAttributes[1];
                String vendorUsername = orderAttributes[2];
                ArrayList<MenuItem> items = parseItems(orderAttributes[3]);
                OrderStatus status = parseOrderStatus(orderAttributes[4]);
                boolean deliveryRequested = Boolean.parseBoolean(orderAttributes[5]); // Ensure this is a boolean
                double total = Double.parseDouble(orderAttributes[6]);
                Customer customer = CustomerDatabase.getCustomer(customerId);

                Vendor vendor = VendorDatabase.getVendor(vendorUsername);
                // Create an Order object
                Order order = new Order(orderId, customer, vendor, items, status, deliveryRequested, total);

                // Add the order to the list
                vendorOrders.add(order);
            }

        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }

        return vendorOrders;
    }

    public static ArrayList<Order> getOrdersByVendorId(String vendorId) {
        ArrayList<Order> vendorOrders = new ArrayList<>();

        try ( FileReader fileReader = new FileReader(filePath);  BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] orderAttributes = line.split(",");

                if (orderAttributes.length >= 3 && orderAttributes[2].equals(vendorId)) {
                    // Extract relevant attributes for creating the Order
                    String orderId = orderAttributes[0];
                    String customerId = orderAttributes[1];
                    String vendorUsername = orderAttributes[2];
                    ArrayList<MenuItem> items = parseItems(orderAttributes[3]);
                    OrderStatus status = parseOrderStatus(orderAttributes[4]);
                    boolean deliveryRequested = Boolean.parseBoolean(orderAttributes[5]); // Ensure this is a boolean
                    double total = Double.parseDouble(orderAttributes[6]);
                    Customer customer = CustomerDatabase.getCustomer(customerId);

                    Vendor vendor = VendorDatabase.getVendor(vendorUsername);
                    // Create an Order object
                    Order order = new Order(orderId, customer, vendor, items, status, deliveryRequested, total);

                    // Add the order to the list
                    vendorOrders.add(order);
                }
            }

        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }

        return vendorOrders;
    }

    public static ArrayList<Order> getOrdersByCustomersId(String customerID) {
        ArrayList<Order> customerOrders = new ArrayList<>();

        for (Order order : getAllOrders()) {
            if (order.getCustomer().getUsername().equals(customerID)) {
                customerOrders.add(order);

            }
        }
        return customerOrders;
    }

// Helper method to parse items from the comma-separated string
    private static ArrayList<MenuItem> parseItems(String itemsString) {
        ArrayList<MenuItem> items = new ArrayList<>();
        String[] itemTokens = itemsString.split("&");

        for (String itemToken : itemTokens) {
            String[] itemAttributes = itemToken.split(":");
            String itemName = itemAttributes[0];
            double itemPrice = Double.parseDouble(itemAttributes[1]);
            items.add(new MenuItem(itemName, itemPrice));
        }

        return items;
    }

    public static Order getOrderbyOrderID(String orderID) {
        Order order = null;

        for (Order or : getAllOrders()) {
            if (or.getOrderId().equals(orderID)) {
                order = or;
            }

        }
        return order;
    }
// Helper method to parse OrderStatus with item information

    private static OrderStatus parseOrderStatus(String statusString) {
        try {
            // Attempt to parse as OrderStatus
            return OrderStatus.valueOf(statusString);
        } catch (IllegalArgumentException e) {
            // If parsing as OrderStatus fails, return an appropriate default status (you can modify this)
            return OrderStatus.PENDING;
        }
    }

    public static void updateOrder(Order updatedOrder) {
        // Read all orders from the file
        ArrayList<Order> allOrders = getAllOrders();
        // Find the index of the order to update based on orderId
        int indexToUpdate = -1;
        for (int i = 0; i < allOrders.size(); i++) {
            if (allOrders.get(i).getOrderId().equals(updatedOrder.getOrderId())) {
                indexToUpdate = i;
                break;
            }
        }
        // If the order is found, update it
        if (indexToUpdate != -1) {
            allOrders.set(indexToUpdate, updatedOrder);

            // Rewrite all orders to the file
            try ( FileWriter fileWriter = new FileWriter(filePath, false); // false to overwrite the file
                      BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

                for (Order order : allOrders) {
                    // Compose a comma-separated string of order attributes
                    String itemsString = order.getItemString(); // Use '&' as the separator
                    String orderString = String.format("%s,%s,%s,%s,%s,%b,%s",
                            order.getOrderId(),
                            order.getCustomer().getUsername(),
                            order.getVendor().getUsername(),
                            itemsString,
                            order.getStatus(),
                            order.isDeliveryRequested(),
                            order.getTotal());

                    // Write the order string to the file
                    bufferedWriter.write(orderString);
                    bufferedWriter.newLine(); // Move to the next line for the next order
                }

            } catch (IOException e) {
                e.printStackTrace(); // Handle the exception according to your needs
            }

            System.out.println("Order updated successfully.");
        } else {
            System.out.println("Order not found for updating.");
        }
    }

    public static void main(String[] args) {
    }
}

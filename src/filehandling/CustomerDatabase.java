package filehandling;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import classes.Customer;
import classes.Order;
import classes.UserType;

public class CustomerDatabase {

    public static void registerCustomer(Customer customer) {
        try ( FileWriter fileWriter = new FileWriter("customers.txt", true);  BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

            // Compose a comma-separated string of customer attributes
            String customerString = String.format("%s,%s,%s,%s,%s,%s,%s,%s",
                    customer.getUserId(),
                    customer.getUsername(),
                    customer.getPassword(),
                    customer.getAddress(),
                    customer.getUserType(),
                    customer.getCredit(),
                    customer.getOrderHistory(),
                    System.lineSeparator());

            // Write the customer string to the file
            bufferedWriter.write(customerString);
            bufferedWriter.close();
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }
    }

    public static ArrayList<Customer> getAllCustomers() {
        ArrayList<Customer> customers = new ArrayList<>();

        try ( FileReader fileReader = new FileReader("customers.txt");  BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] customerAttributes = line.split(",");

                Customer customer = new Customer(
                        customerAttributes[3],
                        Double.parseDouble(customerAttributes[5]),
                        parseOrderHistory(customerAttributes[6]),
                        customerAttributes[0],
                        customerAttributes[1],
                        customerAttributes[2],
                        UserType.valueOf(customerAttributes[4])
                );
                // Add the customer to the list
                customers.add(customer);
            }
            bufferedReader.close();
            fileReader.close();

        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }

        return customers;
    }

    private static ArrayList<Order> parseOrderHistory(String orderHistoryString) {

        ArrayList<Order> orderHistory = new ArrayList<>();
        String[] orderIds = orderHistoryString.split(",");
        for (String orderId : orderIds) {

        }
        return orderHistory;
    }

    public static void deleteCustomerByUsername(String username) {
        ArrayList<Customer> customers = getAllCustomers();

        Iterator<Customer> iterator = customers.iterator();
        while (iterator.hasNext()) {
            Customer customer = iterator.next();
            if (customer.getUsername().equals(username)) {
                iterator.remove();
                writeCustomersToFile(customers);
                return;
            }
        }
    }

    private static void writeCustomersToFile(ArrayList<Customer> customers) {
        try ( FileWriter fileWriter = new FileWriter("customers.txt");  BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

            for (Customer customer : customers) {
                String customerString = String.format("%s,%s,%s,%s,%s,%s,%s,%s",
                        customer.getUserId(),
                        customer.getUsername(),
                        customer.getPassword(),
                        customer.getAddress(),
                        customer.getUserType(),
                        customer.getCredit(),
                        customer.getOrderHistory(),
                        //                        serializeOrderHistory(customer.getOrderHistory()),
                        System.lineSeparator());

                bufferedWriter.write(customerString);
            }
            bufferedWriter.close();
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }
    }

    private static String serializeOrderHistory(ArrayList<Order> orderHistory) {
        // Implement the logic to serialize the order history into a string
        // For simplicity, assuming orderHistory is a list of order IDs
        StringBuilder result = new StringBuilder();
        for (Order order : orderHistory) {
            result.append(order.getOrderId()).append(",");
        }
        return result.toString();
    }

    public static void updateCustomer(Customer updatedCustomer) {
        ArrayList<Customer> customers = getAllCustomers();

        // Check if the customer already exists (based on username)
        Iterator<Customer> iterator = customers.iterator();
        while (iterator.hasNext()) {
            Customer existingCustomer = iterator.next();
            if (existingCustomer.getUsername().equals(updatedCustomer.getUsername())) {
                // Update the existing customer
                existingCustomer.setUserId(updatedCustomer.getUserId());
                existingCustomer.setPassword(updatedCustomer.getPassword());
                existingCustomer.setAddress(updatedCustomer.getAddress());
                existingCustomer.setUserType(updatedCustomer.getUserType());
                existingCustomer.setCredit(updatedCustomer.getCredit());
                existingCustomer.setOrderHistory(updatedCustomer.getOrderHistory());
                writeCustomersToFile(customers);
                return; // Customer updated, so we can exit the method
            }
        }

        // If the customer doesn't exist, you may choose to throw an exception or handle it in some way
        System.out.println("Customer with username " + updatedCustomer.getUsername() + " not found.");

    }
    
    public static Customer login(String username, String password){
        Customer customer = null;
        for (Customer cst : getAllCustomers()) {
            if (cst.getUsername().equals(username) && cst.getPassword().equals(password)) {
                customer = cst;
            }
        }
        return  customer;
    }
   public static Customer getCustomer(String username){
        Customer customer = null;
        for (Customer c : getAllCustomers()) {
            if (c.getUsername().equals(username)) {
                customer = c;
            }
        }     
        return customer;
    }

    public static void main(String[] args) {

   
        CustomerDatabase.deleteCustomerByUsername("john_doe");
        CustomerDatabase.getAllCustomers();
        
//        CustomerDB.deleteCustomerByUsername("user1234");

    }
}

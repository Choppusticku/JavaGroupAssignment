/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filehandling;

import static filehandling.VendorDatabase.earningFilePath;
import static filehandling.VendorDatabase.getAllEarnings;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import classes.DeliveryRunner;
import classes.Order;
import classes.UserType;

public class DeliveryRunnerDatabase {

    static String runnersFilePath = "runners.txt";
    static String filePath = "vendorEarnings.txt";
    static String earnings = "runnerEarnings.txt";
    static String runnersOrders = "runnerOrders.txt";

    public static void createOrUpdateRunnerEarning(String runnerUsername, double earnings) {

        // Read all runner earnings from the file
        ArrayList<String> allEarnings = getAllEarnings();

        // Find the index of the runner's earnings in the list
        int indexToUpdate = -1;
        for (int i = 0; i < allEarnings.size(); i++) {
            String[] earningAttributes = allEarnings.get(i).split(",");
            if (earningAttributes.length >= 1 && earningAttributes[0].equals(runnerUsername)) {
                indexToUpdate = i;
                break;
            }
        }

        // If the runner's earnings are found, update them; otherwise, add new earnings
        if (indexToUpdate != -1) {
            // Update earnings
            double currentEarnings = Double.parseDouble(allEarnings.get(indexToUpdate).split(",")[1]);
            double updatedEarnings = currentEarnings + earnings;
            allEarnings.set(indexToUpdate, String.format("%s,%s", runnerUsername, updatedEarnings));
        } else {
            // Add new earnings
            allEarnings.add(String.format("%s,%s", runnerUsername, earnings));
        }

        // Rewrite all earnings to the file
        writeAllEarnings(allEarnings);
    }

    private static void writeAllEarnings(ArrayList<String> allEarnings) {
        try ( FileWriter fileWriter = new FileWriter(earnings, false); // false to overwrite the file
                  BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

            for (String earning : allEarnings) {
                bufferedWriter.write(earning);
                bufferedWriter.newLine(); // Move to the next line for the next earning
            }

        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }
    }

    public static double getEarning(String username) {
        double earning = 0;
        for (String earn : getAllEarnings()) {
            String[] line = earn.split(",");
            if (username.equals(line[0])) {
                earning = Double.parseDouble(line[1]);
            }
        }

        return earning;
    }

    public static ArrayList<String> getAllEarnings() {
        ArrayList<String> allEarnings = new ArrayList<>();

        try ( FileReader fileReader = new FileReader(earnings);  BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                allEarnings.add(line);
            }

        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }

        return allEarnings;
    }

    public static void createRunner(DeliveryRunner runner) {
        ArrayList<DeliveryRunner> runners = getAllRunners();

        // Check if the runner already exists (based on username)
        Iterator<DeliveryRunner> iterator = runners.iterator();
        while (iterator.hasNext()) {
            DeliveryRunner existingRunner = iterator.next();
            if (existingRunner.getUsername().equals(runner.getUsername())) {
                // Update the existing runner
                existingRunner.setUserId(runner.getUserId());
                existingRunner.setPassword(runner.getPassword());
                existingRunner.setUserType(runner.getUserType());
                existingRunner.setEarnings(runner.getEarnings());
                writeRunnersToFile(runners);
                return; // Runner updated, so we can exit the method
            }
        }

        // If the runner doesn't exist, add a new one
        runners.add(runner);
        writeRunnersToFile(runners);
    }

    public static void updateRunner(DeliveryRunner updatedRunner) {
        ArrayList<DeliveryRunner> runners = getAllRunners();

        // Check if the runner already exists (based on username)
        Iterator<DeliveryRunner> iterator = runners.iterator();
        while (iterator.hasNext()) {
            DeliveryRunner existingRunner = iterator.next();
            if (existingRunner.getUsername().equals(updatedRunner.getUsername())) {
                // Update the existing runner
                existingRunner.setUserId(updatedRunner.getUserId());
                existingRunner.setPassword(updatedRunner.getPassword());
                existingRunner.setUserType(updatedRunner.getUserType());
                existingRunner.setEarnings(updatedRunner.getEarnings());
                existingRunner.setAssignedTasks(updatedRunner.getAssignedTasks());
                writeRunnersToFile(runners);
                return; // Runner updated, so we can exit the method
            }
        }

        // If the runner doesn't exist, you may choose to throw an exception or handle it in some way
        System.out.println("Runner with username " + updatedRunner.getUsername() + " not found.");
    }

    public static void deleteRunnerByUsername(String username) {
        ArrayList<DeliveryRunner> runners = getAllRunners();

        Iterator<DeliveryRunner> iterator = runners.iterator();
        while (iterator.hasNext()) {
            DeliveryRunner runner = iterator.next();
            if (runner.getUsername().equals(username)) {
                iterator.remove();
                writeRunnersToFile(runners);
                return; // Runner deleted, so we can exit the method
            }
        }

        // If the runner doesn't exist, you may choose to throw an exception or handle it in some way
        System.out.println("Runner with username " + username + " not found.");
    }

    public static void displayAllRunners() {
        ArrayList<DeliveryRunner> runners = getAllRunners();

        System.out.println("\nAll Delivery Runners:");
        for (DeliveryRunner runner : runners) {
            System.out.println(runner.getUsername());
        }
    }

    public static void writeRunnersToFile(ArrayList<DeliveryRunner> runners) {
        try ( FileWriter fileWriter = new FileWriter(runnersFilePath);  BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

            for (DeliveryRunner runner : runners) {
                String runnerString = String.format("%s,%s,%s,%s,%s,%s",
                        runner.getUserId(),
                        runner.getUsername(),
                        runner.getPassword(),
                        runner.getUserType(),
                        runner.getEarnings(),
                        System.lineSeparator());

                bufferedWriter.write(runnerString);
            }

        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }
    }

    public static ArrayList<DeliveryRunner> getAllRunners() {
        ArrayList<DeliveryRunner> runners = new ArrayList<>();

        try ( FileReader fileReader = new FileReader(runnersFilePath);  BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] runnerAttributes = line.split(",");
                ArrayList<Order> orders = new ArrayList<>();
              
                for (String assignedOrder : DeliveryRunnerDatabase.getAssignedOrders(runnerAttributes[1])) {
                   
                    Order order = OrderDatabase.getOrderbyOrderID(assignedOrder);
                    orders.add(order);
                }
                

                DeliveryRunner runner = new DeliveryRunner(
                        orders,
                        Double.parseDouble(runnerAttributes[4]), // Assuming earnings is the fifth attribute
                        runnerAttributes[0], // Assuming userId is the first attribute
                        runnerAttributes[1], // Assuming username is the second attribute
                        runnerAttributes[2], // Assuming password is the third attribute
                        UserType.valueOf(runnerAttributes[3]// Assuming UserType is the fourth attribute and is an enum
                        )
                );

                // Add the runner to the list
                runners.add(runner);
            }

        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }

        return runners;
    }

    public static DeliveryRunner getdDeliveryRunner(String username) {
        DeliveryRunner deliveryRunner = null;
        for (DeliveryRunner rn : getAllRunners()) {
            if (rn.getUsername().equals(username)) {
                deliveryRunner = rn;
            }
        }

        return deliveryRunner;
    }

    public static DeliveryRunner login(String username, String password) {
        DeliveryRunner deliveryRunner = null;
        for (DeliveryRunner rn : getAllRunners()) {
            if (rn.getUsername().equals(username) && rn.getPassword().equals(password)) {
                deliveryRunner = rn;
            }
        }

        return deliveryRunner;
    }

    
    public static void addAssignedOrder(String runnerUsername, String orderId) {
        try (FileWriter fileWriter = new FileWriter(runnersOrders, true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

            String assignmentString = String.format("%s,%s", runnerUsername, orderId);

            bufferedWriter.write(assignmentString);
            bufferedWriter.newLine(); // Move to the next line for the next assignment

        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }
    }
    
      public static ArrayList<String> getAssignedOrders(String runnerUsername) {
        ArrayList<String> assignedOrders = new ArrayList<>();

        try (FileReader fileReader = new FileReader(runnersOrders);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] assignmentAttributes = line.split(",");

                if (assignmentAttributes.length >= 2 && assignmentAttributes[0].equals(runnerUsername)) {
                    assignedOrders.add(assignmentAttributes[1]);
                }
            }

        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }

        return assignedOrders;
    }
    public static void main(String[] args) {

        DeliveryRunner newRunner = new DeliveryRunner(
                new ArrayList<>(), // Empty assigned tasks initially
                1000.0, // Initial earnings
                "runner12312", // User ID
                "jane_runner", // Username
                "password123", // Password
                UserType.DELIVERY_RUNNER // UserType
        );

        DeliveryRunnerDatabase.deleteRunnerByUsername("jane_runner");
        DeliveryRunnerDatabase.displayAllRunners();

    }

}

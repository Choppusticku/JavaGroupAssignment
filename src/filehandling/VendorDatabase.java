package filehandling;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import classes.Menu;
import classes.MenuItem;
import classes.UserType;
import classes.Vendor;

public class VendorDatabase {

    static String vendorsFilePath = "vendors.text";
    static String menusFilePath = "menu.text";
    static String earningFilePath = "vendorEarnings.text";

    public static void createOrUpdateVendor(Vendor vendor) {
        ArrayList<Vendor> vendors = getAllVendors();

        // Check if the vendor already exists (based on username)
        Iterator<Vendor> iterator = vendors.iterator();
        while (iterator.hasNext()) {
            Vendor existingVendor = iterator.next();
            if (existingVendor.getUsername().equals(vendor.getUsername())) {
                // Update the existing vendor
                existingVendor.setUserId(vendor.getUserId());
                existingVendor.setPassword(vendor.getPassword());
                existingVendor.setUserType(vendor.getUserType());
                writeVendorsToFile(vendors);

                // Update the menu for the existing vendor (if needed)
                updateMenu(existingVendor.getUsername(), vendor.getMenu());
                return; // Vendor updated, so we can exit the method
            }
        }

        // If the vendor doesn't exist, add a new one
        vendors.add(vendor);
        writeVendorsToFile(vendors);

        // Add the menu for the new vendor
        writeMenu(vendor.getUsername(), vendor.getMenu());
    }

    public static void updateVendor(Vendor updatedVendor) {
        ArrayList<Vendor> vendors = getAllVendors();

        // Check if the vendor already exists (based on username)
        Iterator<Vendor> iterator = vendors.iterator();
        while (iterator.hasNext()) {
            Vendor existingVendor = iterator.next();
            if (existingVendor.getUsername().equals(updatedVendor.getUsername())) {
                // Update the existing vendor
                existingVendor.setUserId(updatedVendor.getUserId());
                existingVendor.setPassword(updatedVendor.getPassword());
                existingVendor.setUserType(updatedVendor.getUserType());
                writeVendorsToFile(vendors);

                // Update the menu for the existing vendor (if needed)
                updateMenu(existingVendor.getUsername(), updatedVendor.getMenu());
                return; // Vendor updated, so we can exit the method
            }
        }

        // If the vendor doesn't exist, you may choose to throw an exception or handle it in some way
        System.out.println("Vendor with username " + updatedVendor.getUsername() + " not found.");
    }

    public static void deleteVendorByUsername(String username) {
        ArrayList<Vendor> vendors = getAllVendors();

        Iterator<Vendor> iterator = vendors.iterator();
        while (iterator.hasNext()) {
            Vendor vendor = iterator.next();
            if (vendor.getUsername().equals(username)) {
                iterator.remove();
                writeVendorsToFile(vendors);

                // Delete the menu for the deleted vendor
                deleteMenu(username);
                return; // Vendor deleted, so we can exit the method
            }
        }

        // If the vendor doesn't exist, you may choose to throw an exception or handle it in some way
        System.out.println("Vendor with username " + username + " not found.");
    }

    public static void displayAllVendors() {
        ArrayList<Vendor> vendors = getAllVendors();

        System.out.println("\nAll Vendors:");
        for (Vendor vendor : vendors) {
            System.out.println(vendor.getMenu().getItems().size());
        }
    }

    public static void writeVendorsToFile(ArrayList<Vendor> vendors) {
        try ( FileWriter fileWriter = new FileWriter(vendorsFilePath);  BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

            for (Vendor vendor : vendors) {
                String vendorString = String.format("%s,%s,%s,%s,%s",
                        vendor.getUserId(),
                        vendor.getUsername(),
                        vendor.getPassword(),
                        vendor.getUserType(),
                        System.lineSeparator());

                bufferedWriter.write(vendorString);
            }

        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }
    }

    public static ArrayList<Vendor> getAllVendors() {
        ArrayList<Vendor> vendors = new ArrayList<>();

        try ( FileReader fileReader = new FileReader(vendorsFilePath);  BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] vendorAttributes = line.split(",");

                Vendor vendor = new Vendor(
                        new Menu(new ArrayList<>()), // Empty menu initially
                        vendorAttributes[0], // Assuming userId is the first attribute
                        vendorAttributes[1], // Assuming username is the second attribute
                        vendorAttributes[2], // Assuming password is the third attribute
                        UserType.valueOf(vendorAttributes[3]) // Assuming UserType is the fourth attribute and is an enum
                );

                // Load the menu for the vendor
                vendor.setMenu(getMenuByUsername(vendor.getUsername()));

                // Add the vendor to the list
                vendors.add(vendor);
            }

        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }

        return vendors;
    }

    private static void writeMenu(String username, Menu menu) {
        // Implement logic to write menu information to the menus file
        // For simplicity, you may choose a format that includes the username and menu details
        // (e.g., "username,menuItem1,menuItem2,...")

        try ( FileWriter fileWriter = new FileWriter(menusFilePath, true);  BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

            StringBuilder menuString = new StringBuilder(username);

            for (MenuItem item : menu.getItems()) {
                menuString.append(",").append(item.getItemName()).append(",").append(item.getPrice());
            }

            menuString.append(System.lineSeparator());
            bufferedWriter.write(menuString.toString());

        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }
    }

    private static void updateMenu(String username, Menu updatedMenu) {

        deleteMenu(username);
        // Then, write the updated menu information
        writeMenu(username, updatedMenu);
    }

    private static void deleteMenu(String username) {
        // Implement logic to delete menu information for the given username from the menus file

        try {
            // Read all lines from the file
            ArrayList<String> lines = new ArrayList<>();
            try ( BufferedReader reader = new BufferedReader(new FileReader(menusFilePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            }

            // Rewrite the file, excluding lines for the specified username
            try ( BufferedWriter writer = new BufferedWriter(new FileWriter(menusFilePath))) {
                for (String line : lines) {
                    if (!line.startsWith(username)) {
                        writer.write(line + System.lineSeparator());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }
    }

    private static Menu getMenuByUsername(String username) {
        Menu menu = new Menu(new ArrayList<>());

        try ( FileReader fileReader = new FileReader(menusFilePath);  BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] menuAttributes = line.split(",");

                if (menuAttributes[0].equals(username)) {
                    for (int i = 1; i < menuAttributes.length; i += 2) {
                        MenuItem menuItem = new MenuItem(menuAttributes[i], Double.parseDouble(menuAttributes[i + 1]));
                        menu.addItem(menuItem);
                    }
                    // No need to break here, in case there are multiple lines for the same username
                }
            }

        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }

        return menu;
    }

    public static Vendor getVendor(String username) {
        Vendor vendor = null;

        for (Vendor v : getAllVendors()) {
            if (v.getUsername().equals(username)) {
                vendor = v;
            }
        }
        return vendor;
    }
    
    public static Vendor loginVendor(String username,String password){
        Vendor vendor = null;
        for (Vendor v : getAllVendors()) {
            if (v.getUsername().equals(username) && v.getPassword().equals(password)) {
                vendor = v;
            }
        }
        return  vendor;
    }
    
      public static void createOrUpdateVendorEarning(String vendorUsername, double earnings) {

        // Read all vendor earnings from the file
        ArrayList<String> allEarnings = getAllEarnings();

        // Find the index of the vendor's earnings in the list
        int indexToUpdate = -1;
        for (int i = 0; i < allEarnings.size(); i++) {
            String[] earningAttributes = allEarnings.get(i).split(",");
            if (earningAttributes.length >= 1 && earningAttributes[0].equals(vendorUsername)) {
                indexToUpdate = i;
                break;
            }
        }

        // If the vendor's earnings are found, update them; otherwise, add new earnings
        if (indexToUpdate != -1) {
            // Update earnings
            double currentEarnings = Double.parseDouble(allEarnings.get(indexToUpdate).split(",")[1]);
            double updatedEarnings = currentEarnings + earnings;
            allEarnings.set(indexToUpdate, String.format("%s,%s", vendorUsername, updatedEarnings));
        } else {
            // Add new earnings
            allEarnings.add(String.format("%s,%s", vendorUsername, earnings));
        }

        // Rewrite all earnings to the file
        writeAllEarnings(allEarnings);
    }

    public static ArrayList<String> getAllEarnings() {
        ArrayList<String> allEarnings = new ArrayList<>();

        try ( FileReader fileReader = new FileReader(earningFilePath);  BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                allEarnings.add(line);
            }

        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }

        return allEarnings;
    }

    private static void writeAllEarnings( ArrayList<String> allEarnings) {
        try ( FileWriter fileWriter = new FileWriter(earningFilePath, false); // false to overwrite the file
                  BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

            for (String earning : allEarnings) {
                bufferedWriter.write(earning);
                bufferedWriter.newLine(); // Move to the next line for the next earning
            }

        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }
    }

     public static double getEarning(String username){
        double earning = 0;
        for (String earn : getAllEarnings()) {
            String[] line = earn.split(",");
            if (username.equals(line[0])) {
                earning = Double.parseDouble(line[1]);
            }
        }
        
        return earning;
    }

    public static void main(String[] args) {
        VendorDatabase.displayAllVendors();
    }

}

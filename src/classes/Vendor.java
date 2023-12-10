package classes;

import java.util.ArrayList;


public class Vendor extends User{
    private Menu menu;

    public Vendor(Menu menu, String userId, String username, String password, UserType userType) {
        super(userId, username, password, userType);
        this.menu = menu;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }
    
    public void removeItem(MenuItem menuItem){
        menu.removeitem(menuItem);
    }

    
    public void addToMenu(MenuItem menuItem){
        menu.addItem(menuItem);
    }
    
    public void updateMenuItem(MenuItem prevItem, MenuItem updatedItem){
        ArrayList<MenuItem> menuItems = menu.getItems();
        for (MenuItem menuItem : menuItems) {
            if (menuItem.getItemName().equals(prevItem.getItemName()) && menuItem.getPrice() == prevItem.getPrice()) {
                menuItem.setItemName(updatedItem.getItemName());
                menuItem.setPrice(updatedItem.getPrice());
            }
        }
        
        menu.setItems(menuItems);
    }
    
    
    

}
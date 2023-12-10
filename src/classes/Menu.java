package classes;

import java.util.ArrayList;

public class Menu {

    private ArrayList<MenuItem> items;

    public Menu(ArrayList<MenuItem> items) {
        this.items = items;
    }

    public ArrayList<MenuItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<MenuItem> items) {
        this.items = items;
    }

    public void addItem(MenuItem menuItem) {
        items.add(menuItem);
    }

    public void removeitem(MenuItem menuItem) {
        int index = -1;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getItemName().equalsIgnoreCase(menuItem.getItemName()) && items.get(i).getPrice() == menuItem.getPrice()) {
                index = i;
            }
        }
        if (index != -1) {
            items.remove(index);

        }
    }

}

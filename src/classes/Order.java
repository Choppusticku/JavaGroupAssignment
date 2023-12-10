package classes;

import java.util.ArrayList;

public class Order {

    private String orderId;
    private Customer customer;
    private Vendor vendor;
    private ArrayList<MenuItem> items;
    private OrderStatus status;
    private boolean deliveryRequested;
    private double total;

    public Order(String orderId, Customer customer, Vendor vendor, ArrayList<MenuItem> items, OrderStatus status, boolean deliveryRequested, double total) {
        this.orderId = orderId;
        this.customer = customer;
        this.vendor = vendor;
        this.items = items;
        this.status = status;
        this.deliveryRequested = deliveryRequested;
        this.total = total;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public ArrayList<MenuItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<MenuItem> items) {
        this.items = items;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public boolean isDeliveryRequested() {
        return deliveryRequested;
    }

    public void setDeliveryRequested(boolean deliveryRequested) {
        this.deliveryRequested = deliveryRequested;
    }

    public String getItemString() {
        StringBuilder itemString = new StringBuilder();

        for (int i = 0; i < items.size(); i++) {
            if (i + 1 == items.size()) {
                itemString.append(items.get(i).getItemName())
                        .append(":")
                        .append(items.get(i).getPrice())
                        .append(",");
            } else {
                itemString.append(items.get(i).getItemName())
                        .append(":")
                        .append(items.get(i).getPrice())
                        .append("&");
            }

        }

        // Remove the trailing comma if there are items
        if (itemString.length() > 0) {
            itemString.deleteCharAt(itemString.length() - 1);
        }

        return itemString.toString();
    }

}

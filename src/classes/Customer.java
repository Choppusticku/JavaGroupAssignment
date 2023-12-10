package classes;

import java.util.ArrayList;

public class Customer extends User {
    private String address;
    private double credit;
    private ArrayList<Order> orderHistory;

 
    public Customer(String address, double credit, ArrayList<Order> orderHistory, String userId, String username, String password, UserType userType) {
        super(userId, username, password, userType);
        this.address = address;
        this.credit = credit;
        this.orderHistory = orderHistory;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

  

 

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public ArrayList<Order> getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(ArrayList<Order> orderHistory) {
        this.orderHistory = orderHistory;
    }
    

}
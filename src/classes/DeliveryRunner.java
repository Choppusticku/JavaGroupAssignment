package classes;

import java.util.ArrayList;

public class DeliveryRunner extends User {

    private ArrayList<Order> assignedTasks;
    private double earnings;

    public DeliveryRunner(ArrayList<Order> assignedTasks, double earnings, String userId, String username, String password, UserType userType) {
        super(userId, username, password, userType);
        this.assignedTasks = assignedTasks;
        this.earnings = earnings;
    }

    public ArrayList<Order> getAssignedTasks() {
        return assignedTasks;
    }

    public void setAssignedTasks(ArrayList<Order> assignedTasks) {
        this.assignedTasks = assignedTasks;
    }

    public double getEarnings() {
        return earnings;
    }

    public void assignNewOrder(Order order) {
        assignedTasks.add(order);
    }

    public void setEarnings(double earnings) {
        this.earnings = earnings;
    }

    public String getTasks() {
        String tasks = "";
        for (int i = 0; i < assignedTasks.size(); i++) {
            if (i + 1 == assignedTasks.size()) {
                tasks += assignedTasks.get(i).getOrderId();

            } else {
                tasks += assignedTasks.get(i).getOrderId() + "&";
            }
        }
        return tasks;
    }

}

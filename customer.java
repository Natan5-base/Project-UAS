import java.util.ArrayList;
public class customer {
private String name;
private ArrayList<order> orders = new ArrayList<>();

public customer(String name) {
this.name = name;
}

public void addOrder(order order) {
orders.add(order);
}

public ArrayList<order> getOrders() {
return orders;
}

public String getName() {
return name;
}
}
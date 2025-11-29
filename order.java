import java.util.ArrayList;

public class order {
private String restaurantName;
private ArrayList<menuItem> items = new ArrayList<>();
private ArrayList<Integer> quantities = new ArrayList<>();

public order(String restaurantName){
this.restaurantName = restaurantName;
}
public String getRestaurantName(){
return restaurantName;
}

public void addItem(menuItem item, int qty) {
items.add(item);
quantities.add(qty);
}

public boolean isEmpty() {
return items.size() == 0;
}

public double calculateTotal() {
double total = 0;
for (int i = 0; i < items.size(); i++) {
total += items.get(i).getPrice() * quantities.get(i);
}
return total;
}

public int totalItemCount() {
int count = 0;
for (int q : quantities) count += q;
return count;
}

public void printOrder() {
System.out.println("\n=== ORDER LIST ===");
for (int i = 0; i < items.size(); i++) {
System.out.println("- " + items.get(i).getName() + " x" + quantities.get(i));
}
}

public ArrayList<menuItem> getItems(){
return items;
}

public ArrayList<Integer> getQuantities(){
return quantities;
}
}
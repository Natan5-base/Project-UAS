import java.util.ArrayList;

public class restaurant {
private String name;
private double rating;
private ArrayList<menuItem> menu;
private int totalRatings;
private double sumRatings;
private static final int MAX_MENU_ITEMS = 5;

public restaurant(String name, double rating, ArrayList<menuItem> menu) {
this.name = name;
this.rating = rating;
this.menu = menu;
this.totalRatings = 1;
this.sumRatings = rating;
}

public void setName(String newName){
    this.name = newName;
}

public void setRating(double newRating){
    this.rating = newRating;
}

public void addRating(double newRating) {
    this.sumRatings += newRating;
    this.totalRatings++;
    this.rating = sumRatings / totalRatings;
}

public String getName() { return name; }
public double getRating() { return rating; }
public ArrayList<menuItem> getMenu() { return menu; }
public int getTotalRatings() { return totalRatings; }
public static int getMaxMenuItems() { return MAX_MENU_ITEMS; }


public void printMenu() {
System.out.println("\n=== " + name + " MENU ===");
for (int i = 0; i < menu.size(); i++) {
menuItem item = menu.get(i);
System.out.printf("%d. %s - Rp %.0f\n", i + 1, item.getName(), item.getPrice());
}
System.out.println("0. Back");
}

// Tambah menu baru
public boolean addMenuItem () {
    return menu.size() < MAX_MENU_ITEMS;
}
}
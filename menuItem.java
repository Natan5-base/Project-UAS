public class menuItem {
private String name;
private double price;
private int sold;
public menuItem(String name, double price) {
this.name = name;
this.price = price;
this.sold = 0;
}

public menuItem(String name, double price, int sold) {
this.name = name;
this.price = price;
this.sold = sold;
}

public String getName() {
return name;
}

public double getPrice() {
return price;
}

public void setPrice(double price) {
this.price = price;
}
public int getSold() {
return sold;
}

public void addSold(int q) { this.sold += q; }
public String toString() {
return name + " | Rp " + price + " | sold: " + sold;
}
}
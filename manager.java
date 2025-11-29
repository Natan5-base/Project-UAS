import java.util.ArrayList;
import java.util.Scanner;

public class manager {
    static Scanner scanner = new Scanner(System.in);

    public static void runManager() {
        ArrayList<restaurant> restaurants = DataStore.restaurants;

        int option;
        do {
            System.out.println("\n MANAGER MAIN MENU ");
            System.out.println("1. Menu");
            System.out.println("2. Incoming Orders (pending dari customer)");
            System.out.println("3. Pesanan Datang (queue driver)");
            System.out.println("4. Complaints");
            System.out.println("0. Back");
            System.out.print("Select option: ");
            option = Integer.parseInt(scanner.nextLine());

            switch (option) {
                case 1:
                    menuSection(restaurants);
                    break;
                case 2:
                    ordersSectionPending();
                    break;
                case 3:
                    ordersSectionDatang();
                    break;
                case 4:
                    showComplaints();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Invalid option!");
            }

        } while (option != 0);
    }

    static void menuSection(ArrayList<restaurant> restaurants) {
        int choice;
        do {
            System.out.println("\n LIST RESTAURANTS ");
            for (int i = 0; i < restaurants.size(); i++) {
                System.out.printf("%d. %s (%.1f)\n", i+1, restaurants.get(i).getName(), restaurants.get(i).getRating());
            }
            System.out.println("0. Back");
            System.out.print("Select restaurant: ");
            choice = Integer.parseInt(scanner.nextLine());
            if (choice == 0) return;
            if (choice < 1 || choice > restaurants.size()) { System.out.println("Invalid"); continue; }
            restaurant res = restaurants.get(choice-1);

            res.printMenu();
            if (!res.getMenu().isEmpty()) {
                menuItem trending = res.getMenu().get(0);
                for (menuItem m : res.getMenu()) { if (m.getSold() > trending.getSold()) trending = m; }
                System.out.println("\nTrending Menu : " + trending.getName() + " (sold: " + trending.getSold() + ")");
            } else {
                System.out.println("\n(no menu items)");
            }

            System.out.println("1. Apply Discount");
            System.out.println("0. Back");
            System.out.print("Select option: ");
            int sub = Integer.parseInt(scanner.nextLine());
            if (sub == 1) applyDiscount(res);

        } while (choice != 0);
    }

    static void applyDiscount(restaurant res) {
        res.printMenu();
        System.out.print("\nSelect menu number: ");
        int idx = Integer.parseInt(scanner.nextLine()) - 1;
        if (idx < 0 || idx >= res.getMenu().size()) { System.out.println("Invalid selection."); return; }
        menuItem item = res.getMenu().get(idx);
        System.out.print("Discount percentage: ");
        double percent = Double.parseDouble(scanner.nextLine());
        double oldPrice = item.getPrice();
        double newPrice = Math.round((oldPrice * (100 - percent) / 100) * 100) / 100.0;
        item.setPrice(newPrice);
        System.out.println("\n DISCOUNT APPLIED ");
        System.out.println("Old Price: Rp " + oldPrice);
        System.out.println("New Price: Rp " + item.getPrice());
    }

    static void ordersSectionPending() {
    System.out.println("\n=== Incoming Pesanan (Pending) ===");

    ArrayList<pesanan> pending = DataStore.pendingPesanan;

    if (pending.isEmpty()) {
        System.out.println("No pending pesanan from customers.");
        return;
    }

    for (int i = 0; i < pending.size(); i++) {
        System.out.print("[" + (i + 1) + "] ");
        pending.get(i).tampilkan();
    }

    System.out.print("Choose index (0 to cancel): ");
    int pick = Integer.parseInt(scanner.nextLine());
    if (pick == 0) { 
        System.out.println("Cancelled."); 
        return; 
    }

    int idx = pick - 1;
    if (idx < 0 || idx >= pending.size()) {
        System.out.println("Invalid index");
        return;
    }

    pesanan p = pending.get(idx);

    System.out.println("1. Accept Full Order");
    System.out.println("2. Accept Partial Order");
    System.out.println("3. Reject Order");
    System.out.print("Choose action: ");
    int action = Integer.parseInt(scanner.nextLine());

    switch (action) {
        case 1:
            p.setStatus("Diterima Manager");
            if (DataStore.driver != null) {
                DataStore.driver.addIncomingOrder(p);
                System.out.println("Order sent to driver.");
            } else {
                DataStore.pesananDatang.add(p);
                System.out.println("No driver registered; order placed in shared queue.");
            }
            pending.remove(idx);
            System.out.println("Full order accepted successfully!");
            break;
            
        case 2:
            System.out.println("\n=== PARTIAL ORDER ACCEPTANCE ===");
            System.out.println("Original Order:");
            p.getOrder().printOrder();
            
            order acceptedOrder = new order(p.getRestoran());
            order rejectedOrder = new order(p.getRestoran());
            
            ArrayList<menuItem> items = p.getOrder().getItems();
            ArrayList<Integer> quantities = p.getOrder().getQuantities();
            
            for (int i = 0; i < items.size(); i++) {
                menuItem item = items.get(i);
                int qty = quantities.get(i);
                
                System.out.print("Accept " + item.getName() + " x" + qty + "? (1=Yes, 0=No): ");
                int accept = Integer.parseInt(scanner.nextLine());
                
                if (accept == 1) {
                    acceptedOrder.addItem(item, qty);
                } else {
                    rejectedOrder.addItem(item, qty);
                    item.addSold(-qty);
                }
            }
            
            if (!acceptedOrder.isEmpty()) {
                pesanan acceptedPesanan = new pesanan(
                    p.getNamaPemesan(),
                    p.getRestoran(),
                    p.getAlamatPemesan(),
                    acceptedOrder
                );
                acceptedPesanan.setStatus("Diterima Manager (Partial)");
                
                if (DataStore.driver != null) {
                    DataStore.driver.addIncomingOrder(acceptedPesanan);
                    System.out.println("Accepted items sent to driver.");
                } else {
                    DataStore.pesananDatang.add(acceptedPesanan);
                    System.out.println("Accepted items placed in queue.");
                }
            }
            
            if (!rejectedOrder.isEmpty()) {
                System.out.println("The following items were rejected:");
                rejectedOrder.printOrder();
            }
            
            pending.remove(idx);
            System.out.println("Partial order processing completed!");
            break;
            
        case 3:
            p.setStatus("Ditolak Manager");
            
            ArrayList<menuItem> rejectedItems = p.getOrder().getItems();
            ArrayList<Integer> rejectedQuantities = p.getOrder().getQuantities();
            
            for (int i = 0; i < rejectedItems.size(); i++) {
                rejectedItems.get(i).addSold(-rejectedQuantities.get(i));
            }
            
            pending.remove(idx);
            System.out.println("Order rejected successfully!");
            break;
            
        default:
            System.out.println("Invalid action!");
    }
}
    static void ordersSectionDatang() {
        ArrayList<pesanan> datang = DataStore.pesananDatang;
        System.out.println("\n=== Pesanan Datang ===");
        if (datang.isEmpty()) { System.out.println("No orders in queue."); return; }
        for (int i = 0; i < datang.size(); i++) {
            System.out.print("[" + (i + 1) + "] ");
            datang.get(i).tampilkan();
        }

        System.out.print("Choose index to mark as 'Sent to driver' (0 cancel): ");
        int pick = Integer.parseInt(scanner.nextLine());
        if (pick == 0) return;
        int idx = pick - 1;
        if (idx < 0 || idx >= datang.size()) { System.out.println("Invalid index"); return; }

        pesanan p = datang.get(idx);
        if (DataStore.driver != null) {
            DataStore.driver.addIncomingOrder(p);
            datang.remove(idx);
            System.out.println("Sent to driver.");
        } else {
            System.out.println("No driver registered. Leave in queue."); }
        }
     static void showComplaints() {
    System.out.println("\n=== CUSTOMER COMPLAINT LIST ===");

    if (DataStore.daftarKomplain.isEmpty()) {
        System.out.println("No complaints submitted.");
        return;
    }

    for (int i = 0; i < DataStore.daftarKomplain.size(); i++) {
        komplain k = DataStore.daftarKomplain.get(i);
        System.out.println("\n[" + (i + 1) + "]");
        System.out.println("Customer : " + k.getCustomerName());
        System.out.println("Restaurant : " + k.getRestaurantName());
        System.out.println("Issue : " + k.getIssue());
        System.out.println("Status : " + k.getStatus());
    }

    System.out.println("\n1. Mark a complaint as resolved");
    System.out.println("0. Back");
    System.out.print("Input: ");
    int choose = Integer.parseInt(scanner.nextLine());

    if (choose == 0) return;

    int idx = choose - 1;
    if (idx < 0 || idx >= DataStore.daftarKomplain.size()) {
        System.out.println("Invalid index.");
        return;
    }

    komplain k = DataStore.daftarKomplain.get(idx);
    k.setStatus("Resolved");

    System.out.println("Complaint marked as resolved.");
}
}

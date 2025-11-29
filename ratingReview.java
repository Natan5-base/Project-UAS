import java.util.Scanner;

public class ratingReview {
    private static Scanner scanner = new Scanner(System.in);
    
    public static void mainRatingReview(customer currentCustomer) {
        if (currentCustomer == null) {
            System.out.println("No customer logged in");
            return;
        }

        // Cek apakah customer punya pesanan yang sudah dikirim
        boolean hasDeliveredOrders = false;
        for (pesanan p : DataStore.pesananAktif) {
            if (p.getNamaPemesan().equals(currentCustomer.getName()) && p.getStatus().equals("Dikirim")) {
                hasDeliveredOrders = true;
                break;
            }
        }

        if (!hasDeliveredOrders) {
            System.out.println("You need to have delivered orders to give rating");
            return;
        }

        System.out.println("\n=== BERI RATING & ULASAN ===");
        
        System.out.print("Nama Restoran: ");
        String restaurantName = scanner.nextLine();
        
        System.out.print("Nama Driver: ");
        String driverName = scanner.nextLine();
        
        System.out.print("Rating Restoran (1-5): ");
        int restRating = Integer.parseInt(scanner.nextLine());
        
        System.out.print("Rating Driver (1-5): ");
        int driverRating = Integer.parseInt(scanner.nextLine());
        
        System.out.print("Ulasan: ");
        String review = scanner.nextLine();

        rating newRating = new rating(currentCustomer.getName(), restaurantName, 
                                 driverName, restRating, driverRating, review);
        DataStore.daftarRating.add(newRating);
        
        System.out.println("Terima kasih atas rating dan ulasannya!");
    }
}

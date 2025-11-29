import java.util.ArrayList;
import java.util.Scanner;

public class mainAppProgram {
    private static Scanner scanner = new Scanner(System.in);

    static customer currentCustomer = null;
    static boolean previousCustomerLogin = false;
    static String sort = "Alphabet";

    public static void main(String[] args) {
        initializeData();
        System.out.println("Selamat datang di " + DataStore.penyedia.getNamaAplikasi() + "!");
        loginMenu();
    }

    public static void loginMenu() {
        int choice = -1; 
        while (true) {
            System.out.println("\n=== MENU LOGIN ===");
            System.out.println("1. Login");
            System.out.println("2. Daftar Sebagai Pelanggan");
            System.out.println("0. Keluar");
            System.out.print("Pilih: ");
            
            try {
                choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1: 
                        if (login()) {
                            redirectBaseOnRole();
                        }
                        break;
                    case 2:
                        registerCustomer();
                        break;
                    case 0:
                        System.out.println("Terima kasih sudah menggunakan " + DataStore.penyedia.getNamaAplikasi() + "!");
                        return;
                    default:
                        System.out.println("Pilihan tidak valid!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Pilihan harus berupa angka!");
            }
        }
    }

    public static boolean login() {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        for (user u : DataStore.users) {
            if (u.getUsername().equals(username) && u.authenticate(password)) {
                DataStore.currentUser = u;
                System.out.println("Login berhasil! Selamat datang " + username + " (" + u.getRole() + ")!");
                if (u.getRole().equals("customer")) {
                    currentCustomer = new customer(username);
                    previousCustomerLogin = true;
                }
                return true;
            }
        }
        System.out.println("Login gagal! Username atau password salah.");
        return false;
    }

    public static void registerCustomer() {
        System.out.print("Pilih Username: ");
        String username = scanner.nextLine();
        System.out.print("Pilih Password: ");
        String password = scanner.nextLine();

        for (user u : DataStore.users) {
            if (u.getUsername().equals(username)) {
                System.out.println("Username sudah terdaftar!");
                return;
            }
        }

        DataStore.users.add(new user(username, password, "customer"));
        System.out.println("Pendaftaran berhasil! Silakan login.");
    }

    public static void redirectBaseOnRole() {
        if (DataStore.currentUser == null) return;
            
        switch (DataStore.currentUser.getRole()) {
            case "customer":
                customerMenu();
                break;
            case "manager":
                manager.runManager();
                break;
            case "driver":
                driverMenu();
                break;
            case "admin":
                adminMenu();
                break;
            default:
                System.out.println("Role tidak dikenali!");
        }
    }

    public static void adminMenu() {
        int choice = -1; 
        do {
            System.out.println("\n=== MENU ADMIN ===");
            System.out.println("1. Rekap Pemasukan");
            System.out.println("2. Kelola Restoran");
            System.out.println("3. Kelola Driver");
            System.out.println("4. Lihat Semua Rating");
            System.out.println("5. Lihat Semua Komplain");
            System.out.println("6. Ubah Komplain ke Rating");
            System.out.println("0. Logout");
            System.out.print("Pilih: ");

            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Pilihan tidak valid!");
                continue;
            }

            switch (choice) {
                case 1:
                    viewFinancialReport();
                    break;
                case 2:
                    manageRestaurants();
                    break;
                case 3:
                    manageDrivers();
                    break;
                case 4:
                    viewAllRatings();
                    break;
                case 5:
                    viewAllComplaints();
                    break;
                case 6:
                    convertComplaintsToRatings();
                    break;
                case 0:
                    DataStore.currentUser = null;
                    System.out.println("Logout berhasil.");
                    break;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        } while (choice != 0);
    }

    public static void viewFinancialReport() {
        System.out.println("\n=== REKAP PEMASUKAN ===");

        double totalRestaurantFees = 0;
        double totalDriverFees = 0;
        int totalOrders = 0;

        for (pesanan p : DataStore.pendingPesanan) {
            double orderTotal = p.getOrder().calculateTotal();
            totalDriverFees += DataStore.penyedia.calculateDriverFee(p.getOngkir());
            totalRestaurantFees += DataStore.penyedia.calculateRestaurantFee(orderTotal);
            totalOrders++;
        }

        for (pesanan p : DataStore.pesananAktif) {
            double orderTotal = p.getOrder().calculateTotal();
            totalDriverFees += DataStore.penyedia.calculateDriverFee(p.getOngkir());
            totalRestaurantFees += DataStore.penyedia.calculateRestaurantFee(orderTotal);
            totalOrders++;
        }

        System.out.println("Total Pesanan: " + totalOrders);
        System.out.printf("Total Biaya Restoran: Rp %.0f%n", totalRestaurantFees);
        System.out.printf("Total Biaya Driver: Rp %.0f%n", totalDriverFees);
        System.out.printf("Total Pendapatan: Rp %.0f%n", totalRestaurantFees + totalDriverFees);
    }

    public static void manageRestaurants() {
        int choice = -1; 
        do {
            System.out.println("\n=== KELOLA RESTORAN ===");
            System.out.println("1. Tambah Restoran");
            System.out.println("2. Lihat Semua Restoran");
            System.out.println("3. Update Restoran");
            System.out.println("4. Hapus Restoran");
            System.out.println("0. Kembali");
            System.out.print("Pilih: ");

            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Pilihan tidak valid!");
                continue;
            }

            switch (choice) {
                case 1:
                    addRestaurant();
                    break;
                case 2:
                    viewAllRestaurants();
                    break;
                case 3:
                    updateRestaurant();
                    break;
                case 4:
                    deleteRestaurant();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        } while (choice != 0);
    }

    public static void addRestaurant() {
        if (DataStore.restaurants.size() >= 20) {
            System.out.println("Maksimal restoran telah tercapai (20)!");
            return;
        }

        System.out.print("Nama Restoran: ");
        String name = scanner.nextLine();
        System.out.print("Rating Awal (1-5): ");
        double rating = Double.parseDouble(scanner.nextLine());

        ArrayList<menuItem> menu = new ArrayList<>();
        DataStore.restaurants.add(new restaurant(name, rating, menu));
        System.out.println("Restoran berhasil ditambahkan!");
    }

    public static void viewAllRestaurants() {
        System.out.println("\n=== SEMUA RESTORAN ===");
        if (DataStore.restaurants.isEmpty()) {
            System.out.println("Tidak ada restoran tersedia.");
            return;
        }

        for (int i = 0; i < DataStore.restaurants.size(); i++) {
            restaurant res = DataStore.restaurants.get(i);
            System.out.printf("%d. %s (%.1f) - %d menu%n", i + 1, res.getName(), res.getRating(), res.getMenu().size());
        }
    }

    public static void updateRestaurant() {
        viewAllRestaurants();
        if (DataStore.restaurants.isEmpty()) return;

        System.out.print("Pilih nomor restoran untuk diupdate: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine()) - 1;
            if (idx < 0 || idx >= DataStore.restaurants.size()) {
                System.out.println("Pilihan tidak valid!");
                return;
            }

            restaurant res = DataStore.restaurants.get(idx);
            System.out.print("Nama baru (kosongkan untuk tidak mengubah): ");
            String newName = scanner.nextLine();
            if (!newName.isEmpty()) {
                res.setName(newName);
            }
            
            System.out.println("Restoran berhasil diupdate!");
            
        } catch (Exception e) {
            System.out.println("Input tidak valid!");
        }
    }

    public static void deleteRestaurant() {
        viewAllRestaurants();
        if (DataStore.restaurants.isEmpty()) return;

        System.out.print("Pilih nomor restoran untuk dihapus: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine()) - 1;
            if (idx < 0 || idx >= DataStore.restaurants.size()) {
                System.out.println("Pilihan tidak valid!");
                return;
            }

            String name = DataStore.restaurants.get(idx).getName();
            DataStore.restaurants.remove(idx);
            System.out.println("Restoran '" + name + "' berhasil dihapus!");
            
        } catch (Exception e) {
            System.out.println("Input tidak valid!");
        }
    }

    public static void manageDrivers() {
        int choice = -1; 
        do {
            System.out.println("\n=== KELOLA DRIVER ===");
            System.out.println("1. Tambah Driver");
            System.out.println("2. Lihat Semua Driver");
            System.out.println("3. Hapus Driver");
            System.out.println("0. Kembali");
            System.out.print("Pilih: ");

            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Pilihan tidak valid!");
                continue;
            }

            switch (choice) {
                case 1:
                    addDriver();
                    break;
                case 2:
                    viewAllDrivers();
                    break;
                case 3:
                    removeDriver();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        } while (choice != 0);
    }

    public static void addDriver() {
        if (DataStore.drivers.size() >= 10) {
            System.out.println("Maksimal driver telah tercapai (10)!");
            return;
        }

        System.out.print("Nama Driver: ");
        String name = scanner.nextLine();
        System.out.print("ID Driver: ");
        String driverId = scanner.nextLine();

        DataStore.drivers.add(new driver(name, driverId));
        System.out.println("Driver berhasil ditambahkan!");
    }

    public static void viewAllDrivers() {
        System.out.println("\n=== SEMUA DRIVER ===");
        if (DataStore.drivers.isEmpty()) {
            System.out.println("Tidak ada driver tersedia.");
            return;
        }

        for (driver d : DataStore.drivers) {
            System.out.println("- " + d.getNamaDriver() + " (" + d.getDriverId() + ")");
        }
    }

    public static void removeDriver() {
        viewAllDrivers();
        if (DataStore.drivers.isEmpty()) return;

        System.out.print("Masukkan ID Driver untuk dihapus: ");
        String driverId = scanner.nextLine();

        driver toRemove = null;
        for (driver d : DataStore.drivers) {
            if (d.getDriverId().equals(driverId)) {
                toRemove = d;
                break;
            }
        }

        if (toRemove != null) {
            DataStore.drivers.remove(toRemove);
            System.out.println("Driver berhasil dihapus!");
        } else {
            System.out.println("Driver tidak ditemukan!");
        }
    }

    public static void viewAllRatings() {
        System.out.println("\n=== SEMUA RATING ===");
        if (DataStore.daftarRating.isEmpty()) {
            System.out.println("Belum ada rating.");
            return;
        }

        for (int i = 0; i < DataStore.daftarRating.size(); i++) {
            rating r = DataStore.daftarRating.get(i);
            System.out.println("\n[" + (i + 1) + "]");
            System.out.println("Pelanggan: " + r.getCustomerName());
            System.out.println("Restoran: " + r.getRestaurantName() + " ⭐" + r.getRestaurantRating());
            System.out.println("Driver: " + r.getDriverName() + " ⭐" + r.getDriverRating());
            System.out.println("Ulasan: " + r.getReview());
        }
    }

    public static void viewAllComplaints() {
        System.out.println("\n=== SEMUA KOMPLAIN ===");
        if (DataStore.daftarKomplain.isEmpty()) {
            System.out.println("Tidak ada komplain.");
            return;
        }

        for (int i = 0; i < DataStore.daftarKomplain.size(); i++) {
            komplain k = DataStore.daftarKomplain.get(i);
            System.out.println("\n[" + (i + 1) + "]");
            System.out.println("Pelanggan: " + k.getCustomerName());
            System.out.println("Restoran: " + k.getRestaurantName());
            System.out.println("Masalah: " + k.getIssue());
            System.out.println("Status: " + k.getStatus());
        }
    }

    public static void convertComplaintsToRatings() {
        System.out.println("\n=== UBAH KOMPLAIN KE RATING ===");

        if (DataStore.daftarKomplain.isEmpty()) {
            System.out.println("Tidak ada komplain untuk diubah.");
            return;
        }

        for (int i = 0; i < DataStore.daftarKomplain.size(); i++) {
            komplain k = DataStore.daftarKomplain.get(i);
            System.out.println("\n[" + (i + 1) + "]");
            System.out.println("Pelanggan: " + k.getCustomerName());
            System.out.println("Restoran: " + k.getRestaurantName());
            System.out.println("Masalah: " + k.getIssue());
            System.out.println("Status: " + k.getStatus());

            System.out.print("Ubah ke rating? (1=Ya, 0=Tidak): ");
            try {
                int convert = Integer.parseInt(scanner.nextLine());

                if (convert == 1) {
                    System.out.print("Rating Restoran (1-5): ");
                    int restRating = Integer.parseInt(scanner.nextLine());
                    System.out.print("Rating Driver (1-5): ");
                    int driverRating = Integer.parseInt(scanner.nextLine());
                    System.out.print("Nama Driver: ");
                    String driverName = scanner.nextLine();

                    rating newRating = new rating(k.getCustomerName(), k.getRestaurantName(), 
                                                driverName, restRating, driverRating, 
                                                "Diubah dari komplain: " + k.getIssue());
                    DataStore.daftarRating.add(newRating);

                    k.setStatus("Diubah ke Rating");
                    System.out.println("Komplain berhasil diubah ke rating!");
                }
            } catch (Exception e) {
                System.out.println("Input tidak valid!");
            }
        }
    }

    public static void initializeData() {
        DataStore.restaurants.add(new restaurant(
                "A&N", 4.5,
                new ArrayList<menuItem>() {{
                    add(new menuItem("Truffle Chicken Alfredo", 68000));
                    add(new menuItem("A&N Signature Beef Bowl", 72000));
                    add(new menuItem("Crispy Salmon Salad", 59000));
                    add(new menuItem("Spicy Korean Wings", 45000));
                    add(new menuItem("Chocolate Lava Soufflé", 38000));
                }}
        ));
        DataStore.restaurants.add(new restaurant(
                "Bebek King", 4.9,
                new ArrayList<menuItem>() {{
                    add(new menuItem("Bebek Goreng Krispi", 55000));
                    add(new menuItem("Nasi Uduk Spesial", 20000));
                    add(new menuItem("Sambal Matah", 15000));
                    add(new menuItem("Paket Bebek Jumbo", 78000));
                    add(new menuItem("Es Teh Manis", 8000));
                }}
        ));
        DataStore.restaurants.add(new restaurant(
                "Suka Maju", 4.6,
                new ArrayList<menuItem>() {{
                    add(new menuItem("Ayam Geprek", 25000));
                    add(new menuItem("Nasi Goreng Spesial", 30000));
                    add(new menuItem("Mie Ayam Bakso", 18000));
                    add(new menuItem("Es Jeruk Segar", 12000));
                    add(new menuItem("Pisang Bakar Coklat", 15000));
                }}
        ));
    }

    public static void customerLogin() {
        if (!previousCustomerLogin) {
            System.out.print("Nama Anda: ");
            currentCustomer = new customer(scanner.nextLine());
            previousCustomerLogin = true;
        }
        System.out.println("Selamat datang " + currentCustomer.getName() + ", apa yang ingin Anda lihat?");
        customerMenu();
    }

    public static void customerMenu() {
        int choice = -1; 
        while (true) {
            System.out.println("\n=== MENU PELANGGAN ===");
            System.out.println("1. Restoran");
            System.out.println("2. Status Pesanan");
            System.out.println("3. Buat Komplain");
            System.out.println("4. Beri Rating & Ulasan");
            System.out.println("0. Logout");
            System.out.print("Pilih: ");

            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Tidak valid!");
                continue;
            }

            switch (choice) {
                case 1: restaurantList(); break;
                case 2: orderStatus(); break;
                case 3: makeComplaint(); break;
                case 4: 
                    if (currentCustomer != null) {
                        ratingReview.mainRatingReview(currentCustomer);
                    } else {
                        System.out.println("Tidak ada pelanggan yang login!");
                    }
                    break;
                case 0: 
                    DataStore.currentUser = null;
                    currentCustomer = null;
                    previousCustomerLogin = false;
                    System.out.println("Logout berhasil!");
                    return;
                default: System.out.println("Tidak valid!");
            }
        }
    }

    public static void makeComplaint() {
        System.out.println("\n=== FORM KOMPLAIN ===");

        System.out.print("Nama Restoran: ");
        String res = scanner.nextLine();

        System.out.print("Jelaskan masalahnya: ");
        String issue = scanner.nextLine();

        komplain k = new komplain(currentCustomer.getName(), res, issue);
        DataStore.daftarKomplain.add(k);

        System.out.println("Komplain Anda telah dikirim!");
    }

    public static void restaurantList() {
        int choice = -1; 
        while (true) {
            System.out.println("\nPilih restoran:");
            for (int i = 0; i < DataStore.restaurants.size(); i++) {
                restaurant res = DataStore.restaurants.get(i);
                System.out.printf("%d. %s (%.1f)%n", i + 1, res.getName(), res.getRating());
            }
            System.out.println("9. Urutkan berdasarkan: " + sort);
            System.out.println("0. Kembali");

            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Tidak valid!");
                continue;
            }

            if (choice == 0) return;
            if (choice == 9){
                if (sort.equals("Alphabet")){
                    sortRestaurantRating();
                    sort = "Rating";
                }
                else{
                    sortRestaurantAlphabet();
                    sort = "Alphabet";
                }
            }
            if (choice < 1 || choice > DataStore.restaurants.size()) {
                System.out.println("Tidak valid!");
                continue;
            }

            orderFlow(DataStore.restaurants.get(choice - 1));
        }
    }

    public static void sortRestaurantRating(){
        for(int i = 0; i < DataStore.restaurants.size() - 1; i++){
            for (int j = 0; j < DataStore.restaurants.size()-1; j++){
                if (DataStore.restaurants.get(j).getRating() < DataStore.restaurants.get(j+1).getRating()){
                    restaurant temp = DataStore.restaurants.get(j);
                    DataStore.restaurants.set(j, DataStore.restaurants.get(j+1));
                    DataStore.restaurants.set(j+1, temp);
                }
            }
        }
        System.out.println("Restoran diurutkan berdasarkan rating!");
    }

    public static void sortRestaurantAlphabet(){
        for(int i = 0; i < DataStore.restaurants.size() - 1; i++){
            for (int j = 0; j < DataStore.restaurants.size()-1; j++){
                if (DataStore.restaurants.get(j).getName().compareTo(DataStore.restaurants.get(j+1).getName()) > 0){
                    restaurant temp = DataStore.restaurants.get(j);
                    DataStore.restaurants.set(j, DataStore.restaurants.get(j+1));
                    DataStore.restaurants.set(j+1, temp);
                }
            }
        }
        System.out.println("Restoran diurutkan berdasarkan abjad!");
    }

    public static void orderFlow(restaurant restaurant) {
        System.out.println("\nSelamat datang di " + restaurant.getName());
        restaurant.printMenu();

        order order = new order(restaurant.getName());

        while (true) {
            System.out.print("Pilih nomor menu, 0 untuk kembali, atau 'selesai': ");
            String input = scanner.nextLine();

            if (input.equals("0")) return;
            if (input.equalsIgnoreCase("selesai")) break;

            int menuChoice;
            try {
                menuChoice = Integer.parseInt(input) - 1;
            } catch (Exception e) {
                System.out.println("Tidak valid!");
                continue;
            }

            if (menuChoice < 0 || menuChoice >= restaurant.getMenu().size()) {
                System.out.println("Tidak valid!");
                continue;
            }

            menuItem selected = restaurant.getMenu().get(menuChoice);
            System.out.print("Jumlah: ");

            int quantity;
            try {
                quantity = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Jumlah tidak valid!");
                continue;
            }

            order.addItem(selected, quantity);
            selected.addSold(quantity);
            System.out.println("Ditambahkan!");
        }

        if (order.isEmpty()) {
            System.out.println("Pesanan kosong!");
            return;
        }

        order.printOrder();
        double total = order.calculateTotal();
        double ongkir = calculateOngkir(order.totalItemCount());

        System.out.printf("\nTOTAL MAKANAN: Rp %.0f%n", total);
        System.out.printf("BIAYA PENGIRIMAN: Rp %.0f%n", ongkir);
        System.out.printf("TOTAL AKHIR: Rp %.0f%n", total + ongkir);
        
        System.out.println("\nMetode Pembayaran:");
        System.out.println("1. Debit (cashback Rp 5000)");
        System.out.println("2. Kredit (diskon 5%)");
        System.out.println("3. Tunai");
        System.out.println("0. Batalkan");

        int paymentMethod;
        try {
            paymentMethod = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("Tidak valid!");
            return;
        }

        if (paymentMethod == 1) total -= 5000;
        else if (paymentMethod == 2) total *= 0.95;
        else if (paymentMethod == 3) {}
        else return;

        double finalTotal = total + ongkir;
        System.out.printf("TOTAL BAYAR: Rp %.0f%n", finalTotal);

        currentCustomer.addOrder(order);

        System.out.print("Masukkan alamat pengiriman: ");
        String alamat = scanner.nextLine();

        pesanan p = new pesanan(currentCustomer.getName(), restaurant.getName(), alamat, order);
        DataStore.pendingPesanan.add(p);

        System.out.println("Pesanan ditempatkan dan menunggu diproses manager.");
        System.out.println("Status: Belum Dimasak");
    }

    public static double calculateOngkir(int itemCount) {
        if (itemCount <= 2) return 10000;
        else if (itemCount <= 5) return 15000;
        else return 20000;
    }

    public static void orderStatus() {
        if (currentCustomer == null) {
            System.out.println("Tidak ada pelanggan yang login");
            return;
        }

        ArrayList<order> orders = currentCustomer.getOrders();
        if (orders.isEmpty()) {
            System.out.println("Anda belum memiliki pesanan");
            return;
        }

        System.out.println("=== PESANAN ANDA ===");
        for (order list : orders) {
            System.out.println("Dari: " + list.getRestaurantName());
            ArrayList<menuItem> items = list.getItems();
            ArrayList<Integer> qty = list.getQuantities();

            for (int j = 0; j < items.size(); j++) {
                System.out.println("- " + items.get(j).getName() + " x" + qty.get(j));
            }
            System.out.println("Total: Rp. " + list.calculateTotal());
            
            for (pesanan p : DataStore.pendingPesanan) {
                if (p.getNamaPemesan().equals(currentCustomer.getName()) && 
                    p.getRestoran().equals(list.getRestaurantName())) {
                    System.out.println("Status: " + p.getStatus());
                    break;
                }
            }
            for (pesanan p : DataStore.pesananAktif) {
                if (p.getNamaPemesan().equals(currentCustomer.getName()) && 
                    p.getRestoran().equals(list.getRestaurantName())) {
                    System.out.println("Status: " + p.getStatus());
                    break;
                }
            }
            System.out.println("------------------------");
        }
    }

    public static void driverMenu() {
        driver currentDriver = null;
        for (driver d : DataStore.drivers) {
            if (d.getNamaDriver().equals(DataStore.currentUser.getUsername())) {
                currentDriver = d;
                break;
            }
        }

        if (currentDriver == null) {
            System.out.println("Driver tidak ditemukan di sistem!");
            return;
        }

        int menu = -1; 

        do {
            System.out.println("\n=========== MENU DRIVER ===========");
            System.out.println("Driver: " + currentDriver.getNamaDriver() + " (" + currentDriver.getDriverId() + ")");
            System.out.println("1. Cek Pesanan Masuk");
            System.out.println("2. Terima / Tolak Pesanan");
            System.out.println("3. Tanda Sudah Diambil");
            System.out.println("4. Tanda Sudah Dikirim");
            System.out.println("5. Lihat Pesanan Aktif");
            System.out.println("6. Lihat Pendapatan");
            System.out.println("0. Logout");
            System.out.print("Pilih menu: ");

            try {
                menu = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Tidak valid!");
                continue;
            }

            switch (menu) {
                case 1:
                    currentDriver.adaPesananDatang();
                    break;

                case 2:
                    currentDriver.adaPesananDatang();
                    if (currentDriver.getPesananDatang().isEmpty()) break;
                    
                    System.out.print("Pilih index: ");
                    int idx = Integer.parseInt(scanner.nextLine()) - 1;

                    System.out.print("Terima (1) / Tolak (0)? ");
                    int acc = Integer.parseInt(scanner.nextLine());

                    currentDriver.pesananDiproses(idx, acc == 1);
                    break;

                case 3:
                    currentDriver.adaPesananAktif();
                    if (currentDriver.getPesananAktif().isEmpty()) break;
                    
                    System.out.print("Index diambil: ");
                    currentDriver.ambilPesanan(Integer.parseInt(scanner.nextLine()) - 1);
                    break;

                case 4:
                    currentDriver.adaPesananAktif();
                    if (currentDriver.getPesananAktif().isEmpty()) break;
                    
                    System.out.print("Index dikirim: ");
                    currentDriver.pesananDikirim(Integer.parseInt(scanner.nextLine()) - 1);
                    break;

                case 5:
                    currentDriver.adaPesananAktif();
                    break;
                    
                case 6:
                    currentDriver.viewEarnings();
                    break;

                case 0:
                    DataStore.currentUser = null;
                    System.out.println("Logout berhasil!");
                    break;

                default:
                    System.out.println("Menu tidak valid.");
            }

        } while (menu != 0);
    }
}
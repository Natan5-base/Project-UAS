import java.util.ArrayList;

public class DataStore {
    public static ArrayList<restaurant> restaurants = new ArrayList<>();
    public static ArrayList<pesanan> pendingPesanan = new ArrayList<>();
    public static ArrayList<pesanan> pesananDatang = new ArrayList<>(); 
    public static ArrayList<pesanan> pesananAktif = new ArrayList<>();  
    public static ArrayList<driver> drivers = new ArrayList<>();
    public static driver driver = null;
    public static ArrayList<komplain> daftarKomplain = new ArrayList<>();
    public static ArrayList<rating> daftarRating = new ArrayList<>();
    public static ArrayList<user> users = new ArrayList<>();
    public static aplikasiPenyedia penyedia = new aplikasiPenyedia("YumDash", 0.1, 0.15);
  
    public static user currentUser = null;

    static {
        users.add(new user("Customer1", "makan357", "customer"));
        users.add(new user("Manager1", "handle589", "manager"));
        users.add(new user("Admin1", "kirim564", "admin"));

        drivers.add(new driver("Saipul", "A 5876 XYU"));
        users.add(new user("Saipul", "driver123", "driver"));
        
        drivers.add(new driver("Jamal", "B 6578 YZU"));
        users.add(new user("Jamal", "driver123", "driver"));
        
        drivers.add(new driver("Udin", "F 8765 CST"));
        users.add(new user("Udin", "driver123", "driver"));
        
        drivers.add(new driver("Budi", "D 7654 YUX"));
        users.add(new user("Budi", "driver123", "driver"));
        
        drivers.add(new driver("Andi", "E 9876 UZT"));
        users.add(new user("Andi", "driver123", "driver"));

        // Driver default (Driver1) untuk kompatibilitas
        drivers.add(new driver("Driver1", "DRV001"));
        users.add(new user("Driver1", "antar894", "driver"));

        driver = drivers.get(0);
    }
}
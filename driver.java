import java.util.ArrayList;

public class driver {

    private String namaDriver;
    private String driverId;
    private ArrayList<pesanan> pesananDatang = new ArrayList<>();
    private ArrayList<pesanan> pesananAktif = new ArrayList<>();
    private double totalEarnings;

    public driver(String namaDriver, String driverId) {
        this.namaDriver = namaDriver;
        this.driverId = driverId;
        this.totalEarnings = 0;
    }

    public driver(String namaDriver) {
        this.namaDriver = namaDriver;
        this.driverId = "DEFAULT_ID";
        this.totalEarnings = 0;
    }

    public String getNamaDriver() {
        return namaDriver;
    }

    public String getDriverId() {
        return driverId;
    }

    public ArrayList<pesanan> getPesananDatang() {
        return pesananDatang;
    }

    public ArrayList<pesanan> getPesananAktif() {
        return pesananAktif;
    }

    public void adaPesananDatang() {
        System.out.println("\n=== |PESANAN MASUK untuk driver: " + namaDriver + "| ===");

        if (pesananDatang.isEmpty()) {
            System.out.println("Tidak ada pesanan menunggu.");
            return;
        }

        for (int i = 0; i < pesananDatang.size(); i++) {
            System.out.print("[" + (i + 1) + "] ");
            pesananDatang.get(i).tampilkan();

            double ongkir = pesananDatang.get(i).getOngkir();
            double driverCut = DataStore.penyedia.getNetForDriver(ongkir);
            System.out.println(" Pendapatan Driver : Rp " + driverCut);
        }
    }

    public void pesananDiproses(int index, boolean terima) {

        if (index < 0 || index >= pesananDatang.size()) {
            System.out.println("Index tidak valid.");
            return;
        }

        pesanan p = pesananDatang.get(index);

        if (terima) {
            p.setStatus("Diterima");
            p.setDriverId(this.driverId); 
            pesananAktif.add(p);
            System.out.println("\n> Pesanan diterima oleh driver " + namaDriver);
        } else {
            p.setStatus("Ditolak");
            System.out.println("\n> Pesanan ditolak oleh driver " + namaDriver);
            DataStore.pesananDatang.add(p);
        }

        pesananDatang.remove(index);
    }

    public void ambilPesanan(int index) {

        if (index < 0 || index >= pesananAktif.size()) {
            System.out.println("Index tidak valid.");
            return;
        }

        pesanan p = pesananAktif.get(index);

        if (p.getStatus().equals("Diterima")) {
            p.setStatus("Diambil");
            System.out.println("> " + namaDriver + " mengambil pesanan!");
        } else {
            System.out.println("> Pesanan belum bisa diambil.");
        }
    }

    public void pesananDikirim(int index) {

        if (index < 0 || index >= pesananAktif.size()) {
            System.out.println("Index tidak valid.");
            return;
        }

        pesanan p = pesananAktif.get(index);

        if (p.getStatus().equals("Diambil")) {
            p.setStatus("Dikirim");
            double ongkir = p.getOngkir();
            double driverCut = DataStore.penyedia.getNetForDriver(ongkir);
            totalEarnings += driverCut;
            System.out.println("> " + namaDriver + " mengantarkan pesanan ke pemesan!");
            System.out.println("> Pendapatan dari pesanan ini: Rp " + driverCut);
            pesananAktif.remove(index);
        } else {
            System.out.println("> Pesanan belum diambil.");
        }
    }

    public void adaPesananAktif() {
        System.out.println("\n=== |PESANAN AKTIF Driver: " + namaDriver + "| ===");

        if (pesananAktif.isEmpty()) {
            System.out.println("Tidak ada pesanan aktif.");
            return;
        }

        for (int i = 0; i < pesananAktif.size(); i++) {
            System.out.print("[" + (i + 1) + "] ");
            pesananAktif.get(i).tampilkan();
        }
    }

    public void tambahPesananDatang(pesanan p) {
        pesananDatang.add(p);
    }
    public void addIncomingOrder(pesanan p) {
        pesananDatang.add(p);
    }

    public void viewEarnings() {
        System.out.println("\nTotal pendapatan driver " + namaDriver + ": Rp " + totalEarnings);

        double pendingEarnings = 0;
        for (pesanan p : pesananAktif) {
            if (p.getDriverId() != null && p.getDriverId().equals(driverId)) { 
                double ongkir = p.getOngkir();
                double driverCut = DataStore.penyedia.getNetForDriver(ongkir);
                pendingEarnings += driverCut;
            }
        }
        System.out.printf("Pendapatan tertunda dari pesanan aktif: Rp %.0f\n", pendingEarnings);
    }
}

public class aplikasiPenyedia {
    private String namaAplikasi;
    private double feeRestaurant;
    private double feeDriver;

    public aplikasiPenyedia(String namaAplikasi, double feeRestaurant, double feeDriver) {
        this.namaAplikasi = namaAplikasi;
        this.feeRestaurant = feeRestaurant;
        this.feeDriver = feeDriver;
    }

    //GET Mehtods
    public String getNamaAplikasi() { return namaAplikasi; }
    public double getFeeRestaurant() { return feeRestaurant; }
    public double getFeeDriver() { return feeDriver; }

    //Hitung fee
    public double calculateRestaurantFee(double totalPesanan) {
        return totalPesanan * feeRestaurant;
    }

    public double calculateDriverFee(double ongkir) {
        return ongkir * feeDriver;
    }

    public double getNetForRestaurant(double totalPesanan) {
        return totalPesanan - calculateRestaurantFee(totalPesanan);
    }

    public double getNetForDriver(double ongkir) {
        return ongkir - calculateDriverFee(ongkir);
    }
}
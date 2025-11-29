public class pesanan {
    private String namaPemesan;
    private String restoran;
    private String alamatPemesan;
    private String status;
    private order order;
    private double ongkir;
    private String driverId;

    public String getAlamatPemesan() {
        return alamatPemesan;
    }

    public pesanan(String namaPemesan, String restoran, String alamatPemesan, order order) {
        this.namaPemesan = namaPemesan;
        this.restoran = restoran;
        this.alamatPemesan = alamatPemesan;
        this.order = order;
        this.status = "Belum Dimasak";
        this.ongkir = calculateOngkir();
        this.driverId = null;
    }

    private double calculateOngkir() {
        int itemCount = order.totalItemCount();
        if (itemCount <= 2) 
            return 10000;
        else if (itemCount <= 5) 
            return 15000;
        else 
            return 20000;
    }
    

    public void tampilkan() {
        System.out.println("Nama Pemesan : " + namaPemesan);
        System.out.println("Restoran : " + restoran);
        System.out.println("Alamat Pemesan: " + alamatPemesan);
        System.out.println("Status : " + status);
        System.out.println("Ongkir : Rp " + ongkir);
        System.out.println("Total Pesanan : Rp " + order.totalItemCount());
        System.out.println("--------------------------------");
    }

    public String getStatus() { return status; }
    public void setStatus(String s) { status = s; }
    public order getOrder() { return order; }
    public String getNamaPemesan() { return namaPemesan; }
    public String getRestoran() { return restoran; }
    public double getOngkir() { return ongkir; }
    public String getDriverId() { return driverId; }
    public void setDriverId(String driverId) { this.driverId = driverId; }
}

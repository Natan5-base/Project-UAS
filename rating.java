public class rating {
    private String customerName;
    private String restaurantName;
    private String driverName;
    private int restaurantRating;
    private int driverRating;
    private String review;
    private boolean fromComplaint;

    //Constructor
        public rating(String customerName, String restaurantName, String driverName, 
                  int restaurantRating, int driverRating, String review) {
        this.customerName = customerName;
        this.restaurantName = restaurantName;
        this.driverName = driverName;
        this.restaurantRating = restaurantRating;
        this.driverRating = driverRating;
        this.review = review;
        this.fromComplaint = false;
    }
    public rating(komplain complaint, int restaurantRating, int driverRating) {
        this.customerName = complaint.getCustomerName();
        this.restaurantName = complaint.getRestaurantName();
        this.driverName = "Unknown"; 
        this.restaurantRating = restaurantRating;
        this.driverRating = driverRating;
        this.review = "Converted from complaint: " + complaint.getIssue();
        this.fromComplaint = true;
    }
    // Ini untuk METHOD GET
    public String getCustomerName() { return customerName; }
    public String getRestaurantName() { return restaurantName; }
    public String getDriverName() { return driverName; }
    public int getRestaurantRating() { return restaurantRating; }
    public int getDriverRating() { return driverRating; }
    public String getReview() { return review; }
    public boolean isFromComplaint() { return fromComplaint; }
    
    public void setDriverName(String driverName) { this.driverName = driverName; }
}   
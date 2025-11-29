public class komplain {
    private String customerName;
    private String restaurantName;
    private String issue;
    private String status = "Pending";

    public komplain(String cust, String rest, String issue) {
        this.customerName = cust;
        this.restaurantName = rest;
        this.issue = issue;
    }

    public String getCustomerName() { return customerName; }
    public String getRestaurantName() { return restaurantName; }
    public String getIssue() { return issue; }
    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }
}



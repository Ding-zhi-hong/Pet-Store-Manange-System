import java.util.List ;
import java.time.LocalDateTime;
class Order {

    private String customerId;
    private String pet;
    private String status; // PENDING, COMPLETED, CANCELLED
    private LocalDateTime orderDate;
    Order(String customerId, String pet, String status, LocalDateTime orderDate) {
        this.customerId = customerId;
        this.status = status;
        this.orderDate = orderDate;
        this.pet = pet;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getCustomerId() {
        return customerId;
    }
    public String getPet() {
        return pet;
    }
    public String getStatus() {
        return status;
    }
    public LocalDateTime getOrderDate() {
        return orderDate;
    }
}



import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order implements Payable, Billable {
    private String orderId;
    private Student customer;
    private List<MenuItem> items;
    private List<Integer> quantities;
    private OrderStatus status;
    private LocalDateTime createdTime;

    public Order(String orderId, Student customer) {
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new IllegalArgumentException("Order ID cannot be empty.");
        }
        this.orderId = orderId;
        this.customer = customer;
        this.items = new ArrayList<MenuItem>();
        this.quantities = new ArrayList<Integer>();
        this.status = OrderStatus.PENDING;
        this.createdTime = LocalDateTime.now();
    }

    public String getOrderId() {
        return orderId;
    }

    public Student getCustomer() {
        return customer;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public List<MenuItem> getItems() {
        return new ArrayList<MenuItem>(items);
    }

    public List<Integer> getQuantities() {
        return new ArrayList<Integer>(quantities);
    }

    public void addItem(MenuItem item, int quantity) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }

        int index = items.indexOf(item);
        if (index >= 0) {
            // Item already in list → increase quantity
            int currentQty = quantities.get(index);
            quantities.set(index, currentQty + quantity);
        } else {
            items.add(item);
            quantities.add(quantity);
        }
    }

    public void updateQuantity(MenuItem item, int newQuantity) {
        int index = items.indexOf(item);
        if (index < 0) {
            throw new IllegalArgumentException("Item not found in order.");
        }
        if (newQuantity <= 0) {
            // remove if quantity becomes zero or negative
            items.remove(index);
            quantities.remove(index);
        } else {
            quantities.set(index, newQuantity);
        }
    }

    public void clearItems() {
        items.clear();
        quantities.clear();
    }

    public void removeItem(int index) {
        if (index < 0 || index >= items.size()) {
            throw new IllegalArgumentException("Invalid item index.");
        }
        // Decrement quantity by 1
        int currentQty = quantities.get(index);
        if (currentQty > 1) {
            quantities.set(index, currentQty - 1);
        } else {
            // Remove item completely if quantity becomes 0
            items.remove(index);
            quantities.remove(index);
        }
    }

    public void setStatus(OrderStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("Status cannot be null.");
        }
        this.status = newStatus;
    }

    public int getTotalItemCount() {
        int total = 0;
        for (int qty : quantities) {
            total += qty;
        }
        return total;
    }

    // -------- Billable implementation --------
    @Override
    public double calculateBillAmount() {
        double total = 0.0;
        for (int i = 0; i < items.size(); i++) {
            MenuItem item = items.get(i);
            int qty = quantities.get(i);
            total += item.getUnitPrice() * qty;
        }
        return total;
    }

    @Override
    public String getBillDescription() {
        return "Canteen Order #" + orderId;
    }

    // -------- Payable implementation --------
    @Override
    public double getAmountToPay() {
        return calculateBillAmount();
    }

    @Override
    public String getPayerId() {
        if (customer == null) {
            return "UNKNOWN";
        }
        return customer.getUserId();
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + orderId + '\'' +
                ", customer=" + (customer != null ? customer.getName() : "N/A") +
                ", items=" + items.size() +
                ", total=" + calculateBillAmount() +
                ", status=" + status +
                '}';
    }
}

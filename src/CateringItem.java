
public class CateringItem
{
    private MenuItem menuItem;
    private int quantity;

    public CateringItem(MenuItem menuItem, int quantity) {
        if (menuItem == null) {
            throw new IllegalArgumentException("Menu item cannot be null.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }
        this.menuItem = menuItem;
        this.quantity = quantity;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }
        this.quantity = quantity;
    }

    public double getSubTotal() {
        return menuItem.getUnitPrice() * quantity;
    }

    @Override
    public String toString() {
        return menuItem.getItemName() + " x " + quantity +
                " = " + getSubTotal() + " AED";
    }
}

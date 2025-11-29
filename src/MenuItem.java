

import java.text.DecimalFormat;

public class MenuItem implements Orderable
{
    private int itemId;
    private String name;
    private double price;
    private String category; // e.g. "Main", "Snack", "Drink", "Dessert"

    private static final DecimalFormat PRICE_FORMAT = new DecimalFormat("0.00");

    public MenuItem(int itemId, String name, double price, String category) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        this.itemId = itemId;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    @Override
    public String getItemName() {
        return name;
    }

    public void setItemName(String name) {
        this.name = name;
    }

    @Override
    public double getUnitPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return itemId + " - " + name + " (" + category + ") : "
                + PRICE_FORMAT.format(price) + " AED";
    }
}

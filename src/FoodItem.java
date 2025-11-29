
public class FoodItem extends MenuItem
{
    private boolean vegetarian;
    private boolean spicy;

    public FoodItem(int itemId, String name, double price,
                    String category, boolean vegetarian, boolean spicy) {
        super(itemId, name, price, category);
        this.vegetarian = vegetarian;
        this.spicy = spicy;
    }

    public boolean isVegetarian() {
        return vegetarian;
    }

    public void setVegetarian(boolean vegetarian) {
        this.vegetarian = vegetarian;
    }

    public boolean isSpicy() {
        return spicy;
    }

    public void setSpicy(boolean spicy) {
        this.spicy = spicy;
    }

    @Override
    public String toString() {
        String vegText = vegetarian ? "Veg" : "Non-Veg";
        String spiceText = spicy ? "Spicy" : "Not Spicy";
        return super.toString() + " [" + vegText + ", " + spiceText + "]";
    }
}

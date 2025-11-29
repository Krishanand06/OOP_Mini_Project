
public class DrinkItem extends MenuItem
{
    private boolean cold;
    private boolean sugarFree;

    public DrinkItem(int itemId, String name, double price,
                     String category, boolean cold, boolean sugarFree) {
        super(itemId, name, price, category);
        this.cold = cold;
        this.sugarFree = sugarFree;
    }

    public boolean isCold() {
        return cold;
    }

    public void setCold(boolean cold) {
        this.cold = cold;
    }

    public boolean isSugarFree() {
        return sugarFree;
    }

    public void setSugarFree(boolean sugarFree) {
        this.sugarFree = sugarFree;
    }

    @Override
    public String toString() {
        String tempText = cold ? "Cold" : "Hot";
        String sugarText = sugarFree ? "Sugar-free" : "Normal sugar";
        return super.toString() + " [" + tempText + ", " + sugarText + "]";
    }
}

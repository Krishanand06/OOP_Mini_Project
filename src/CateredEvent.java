

import java.util.ArrayList;
import java.util.List;

public class CateredEvent extends Event implements Payable, Billable
{
    private List<CateringItem> cateringItems;

    public CateredEvent(String eventId, String name, String clubName,
                        String organiserId, String date, String time,
                        String venue, int expectedAttendees) {
        super(eventId, name, clubName, organiserId, date, time, venue, expectedAttendees);
        this.cateringItems = new ArrayList<CateringItem>();
        setStatus(CateringStatus.SCHEDULED);
    }

    public List<CateringItem> getCateringItems() {
        return new ArrayList<CateringItem>(cateringItems);
    }

    public void addCateringItem(MenuItem item, int quantity) {
        if (item == null) {
            throw new IllegalArgumentException("Menu item cannot be null.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }

        // If the same item already exists, just increase quantity
        for (CateringItem ci : cateringItems) {
            if (ci.getMenuItem().equals(item)) {
                ci.setQuantity(ci.getQuantity() + quantity);
                return;
            }
        }
        cateringItems.add(new CateringItem(item, quantity));
    }

    public void updateItemQuantity(MenuItem item, int newQuantity) {
        if (item == null) {
            throw new IllegalArgumentException("Menu item cannot be null.");
        }

        for (int i = 0; i < cateringItems.size(); i++) {
            CateringItem ci = cateringItems.get(i);
            if (ci.getMenuItem().equals(item)) {
                if (newQuantity <= 0) {
                    cateringItems.remove(i);
                } else {
                    ci.setQuantity(newQuantity);
                }
                return;
            }
        }
        throw new IllegalArgumentException("Item not found in catering list.");
    }

    public void clearCateringItems() {
        cateringItems.clear();
    }

    // ---------- Billable ----------
    @Override
    public double calculateBillAmount() {
        double total = 0.0;
        for (CateringItem ci : cateringItems) {
            total += ci.getSubTotal();
        }
        return total;
    }

    @Override
    public String getBillDescription() {
        return "Catered Event: " + getName() + " on " + getDate();
    }

    // ---------- Payable ----------
    @Override
    public double getAmountToPay() {
        return calculateBillAmount();
    }

    @Override
    public String getPayerId() {
        // clubName or organiserId can be used; we use organiserId here
        return getOrganiserId();
    }

    @Override
    public String toString() {
        return super.toString() + " | Catering items: " + cateringItems.size() +
                " | Total cost: " + calculateBillAmount() + " AED";
    }
}


public abstract class MealPlan
{
    protected String planName;
    protected double monthlyCost;
    protected String description;

    public MealPlan(String planName, double monthlyCost, String description) {
        this.planName = planName;
        this.monthlyCost = monthlyCost;
        this.description = description;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public double getMonthlyCost() {
        return monthlyCost;
    }

    public void setMonthlyCost(double monthlyCost) {
        this.monthlyCost = monthlyCost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Each specific plan (Basic, Premium, Weekend) will implement this.
    public abstract double calculateMonthlyBill(int daysOnLeave);

    @Override
    public String toString() {
        return planName + " : " + monthlyCost + " AED/month";
    }
}

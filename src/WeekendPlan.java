
public class WeekendPlan extends MealPlan
{
    public WeekendPlan() {
        super("Weekend Plan", 200.0,
              "Plan for students who eat in mess only on weekends.");
    }

    @Override
    public double calculateMonthlyBill(int daysOnLeave) {
        if (daysOnLeave < 0 || daysOnLeave > 10) {
            throw new IllegalArgumentException("Invalid number of leave days for weekend plan.");
        }

        // Assume 8 weekend days per month, refund per missed weekend
        double perDayCost = monthlyCost / 8.0;
        double discount = perDayCost * daysOnLeave;
        double bill = monthlyCost - discount;
        return bill < 0 ? 0 : bill;
    }
}

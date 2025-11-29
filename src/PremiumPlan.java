
public class PremiumPlan extends MealPlan
{
    public PremiumPlan() {
        super("Premium Plan", 500.0,
              "Premium mess plan with extra dishes and desserts.");
    }

    @Override
    public double calculateMonthlyBill(int daysOnLeave) {
        if (daysOnLeave < 0 || daysOnLeave > 31) {
            throw new IllegalArgumentException("Invalid number of leave days.");
        }

        // 70% refund per leave day (higher fixed costs)
        double perDayCost = monthlyCost / 30.0;
        double discount = perDayCost * daysOnLeave * 0.7;
        double bill = monthlyCost - discount;
        return bill < 0 ? 0 : bill;
    }
}

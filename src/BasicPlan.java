
public class BasicPlan extends MealPlan
{
    public BasicPlan() {
        super("Basic Plan", 350.0, "Standard mess plan with simple meals.");
    }

    @Override
    public double calculateMonthlyBill(int daysOnLeave) {
        if (daysOnLeave < 0 || daysOnLeave > 31) {
            throw new IllegalArgumentException("Invalid number of leave days.");
        }

        // Full refund for each leave day
        double perDayCost = monthlyCost / 30.0;
        double discount = perDayCost * daysOnLeave;
        double bill = monthlyCost - discount;
        return bill < 0 ? 0 : bill;
    }
}


public class Hosteller extends Student {
    private MealPlan mealPlan;

    public Hosteller(String userId, String name, String email, String rollNo, MealPlan mealPlan) {
        super(userId, name, email, rollNo, "Hosteller");
        this.mealPlan = mealPlan;
    }

    public MealPlan getMealPlan() {
        return mealPlan;
    }

    public void setMealPlan(MealPlan mealPlan) {
        this.mealPlan = mealPlan;
    }

    @Override
    public String getUserType() {
        return "Hosteller Student";
    }
}

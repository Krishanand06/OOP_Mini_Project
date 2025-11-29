
public class CanteenStaff extends Staff
{
    public CanteenStaff(String userId, String name, String email) {
        super(userId, name, email, "Canteen Staff");
    }

    @Override
    public String getUserType() {
        return "Canteen Staff";
    }
}

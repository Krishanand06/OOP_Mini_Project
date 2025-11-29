
public class MessStaff extends Staff
{
    public MessStaff(String userId, String name, String email) {
        super(userId, name, email, "Mess Staff");
    }

    @Override
    public String getUserType() {
        return "Mess Staff";
    }
}

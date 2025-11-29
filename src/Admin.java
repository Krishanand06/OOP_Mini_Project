
public class Admin extends Staff
{
    public Admin(String userId, String name, String email) {
        super(userId, name, email, "Admin");
    }

    @Override
    public String getUserType() {
        return "Admin";
    }
}

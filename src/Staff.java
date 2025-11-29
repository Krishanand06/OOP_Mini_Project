
public class Staff extends User
{
    protected String role;   // e.g. "Canteen Staff", "Mess Staff", "Admin"

    public Staff(String userId, String name, String email, String role) {
        super(userId, name, email);
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String getUserType() {
        return "Staff";
    }
}

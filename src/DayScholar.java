
public class DayScholar extends Student {
    public DayScholar(String userId, String name, String email, String rollNo) {
        super(userId, name, email, rollNo, "DayScholar");
    }

    @Override
    public String getUserType() {
        return "Day Scholar Student";
    }
}

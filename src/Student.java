
public class Student extends User {
    protected String rollNo;
    protected String hostelStatus; // "Hosteller" or "DayScholar"

    public Student(String userId, String name, String email, String rollNo, String hostelStatus) {
        super(userId, name, email);
        this.rollNo = rollNo;
        this.hostelStatus = hostelStatus;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getHostelStatus() {
        return hostelStatus;
    }

    public void setHostelStatus(String hostelStatus) {
        this.hostelStatus = hostelStatus;
    }

    @Override
    public String getUserType() {
        return "Student";
    }
}

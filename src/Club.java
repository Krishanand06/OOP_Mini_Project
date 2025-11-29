
/**
 * Represents a student club/organization on campus.
 * Clubs can organize catering events.
 */
public class Club {
    private String clubId;
    private String clubName;
    private String description;
    private String presidentId; // Student ID of the club president

    public Club(String clubId, String clubName, String description, String presidentId) {
        this.clubId = clubId;
        this.clubName = clubName;
        this.description = description;
        this.presidentId = presidentId;
    }

    public String getClubId() {
        return clubId;
    }

    public void setClubId(String clubId) {
        this.clubId = clubId;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPresidentId() {
        return presidentId;
    }

    public void setPresidentId(String presidentId) {
        this.presidentId = presidentId;
    }

    @Override
    public String toString() {
        return clubName + " (ID: " + clubId + ")";
    }
}

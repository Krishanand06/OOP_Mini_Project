

public class Event
{
    private String eventId;
    private String name;
    private String clubName;        // e.g. "GDG BPDC", "MTC"
    private String organiserId;     // club representative's userId
    private String date;            // simple String: "05-12-2025"
    private String time;            // "18:00"
    private String venue;
    private int expectedAttendees;
    private CateringStatus status;

    public Event(String eventId, String name, String clubName,
                 String organiserId, String date, String time,
                 String venue, int expectedAttendees) {

        if (eventId == null || eventId.trim().isEmpty()) {
            throw new IllegalArgumentException("Event ID cannot be empty.");
        }
        this.eventId = eventId;
        this.name = name;
        this.clubName = clubName;
        this.organiserId = organiserId;
        this.date = date;
        this.time = time;
        this.venue = venue;
        this.expectedAttendees = expectedAttendees;
        this.status = CateringStatus.PLANNED;
    }

    public String getEventId() {
        return eventId;
    }

    public String getName() {
        return name;
    }

    public String getClubName() {
        return clubName;
    }

    public String getOrganiserId() {
        return organiserId;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getVenue() {
        return venue;
    }

    public int getExpectedAttendees() {
        return expectedAttendees;
    }

    public CateringStatus getStatus() {
        return status;
    }

    public void setStatus(CateringStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null.");
        }
        this.status = status;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id='" + eventId + '\'' +
                ", name='" + name + '\'' +
                ", club='" + clubName + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", venue='" + venue + '\'' +
                ", expectedAttendees=" + expectedAttendees +
                ", status=" + status +
                '}';
    }
}

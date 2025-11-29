

import java.time.LocalDateTime;

public class Feedback
{
    private String feedbackId;
    private String studentId;
    private FeedbackCategory category;
    private int rating;          // 1–5
    private String comments;

    // Optional sub-ratings
    private int tasteRating;     // -1 if not given
    private int hygieneRating;   // -1 if not given
    private int priceRating;     // -1 if not given
    private int varietyRating;   // -1 if not given

    private LocalDateTime timestamp;

    public Feedback(String feedbackId, String studentId,
                    FeedbackCategory category, int rating,
                    String comments) {

        this(feedbackId, studentId, category, rating,
             comments, -1, -1, -1, -1);
    }

    public Feedback(String feedbackId, String studentId,
                    FeedbackCategory category, int rating,
                    String comments,
                    int tasteRating, int hygieneRating,
                    int priceRating, int varietyRating) {

        if (feedbackId == null || feedbackId.trim().isEmpty()) {
            throw new IllegalArgumentException("Feedback ID cannot be empty.");
        }
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Student ID cannot be empty.");
        }
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null.");
        }
        if (!isValidMainRating(rating)) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
        if (comments == null || comments.trim().isEmpty()) {
            throw new IllegalArgumentException("Comments cannot be empty.");
        }

        this.feedbackId = feedbackId;
        this.studentId = studentId;
        this.category = category;
        this.rating = rating;
        this.comments = comments.trim();

        this.tasteRating = normalizeSubRating(tasteRating);
        this.hygieneRating = normalizeSubRating(hygieneRating);
        this.priceRating = normalizeSubRating(priceRating);
        this.varietyRating = normalizeSubRating(varietyRating);

        this.timestamp = LocalDateTime.now();
    }

    private boolean isValidMainRating(int r) {
        return r >= 1 && r <= 5;
    }

    private int normalizeSubRating(int r) {
        if (r == -1) {
            return -1; // not provided
        }
        if (r < 1 || r > 5) {
            throw new IllegalArgumentException("Sub-rating must be between 1 and 5 or -1.");
        }
        return r;
    }

    public String getFeedbackId() {
        return feedbackId;
    }

    public String getStudentId() {
        return studentId;
    }

    public FeedbackCategory getCategory() {
        return category;
    }

    public int getRating() {
        return rating;
    }

    public String getComments() {
        return comments;
    }

    public int getTasteRating() {
        return tasteRating;
    }

    public int getHygieneRating() {
        return hygieneRating;
    }

    public int getPriceRating() {
        return priceRating;
    }

    public int getVarietyRating() {
        return varietyRating;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "id='" + feedbackId + '\'' +
                ", studentId='" + studentId + '\'' +
                ", category=" + category +
                ", rating=" + rating +
                ", comments='" + comments + '\'' +
                ", time=" + timestamp +
                '}';
    }

    /**
     * Returns a simple line format suitable for saving in a text file.
     */
    public String toFileString() {
        return feedbackId + "|" + studentId + "|" + category + "|" +
               rating + "|" + tasteRating + "|" + hygieneRating + "|" +
               priceRating + "|" + varietyRating + "|" +
               timestamp + "|" + comments.replace("|", "/");
    }
}

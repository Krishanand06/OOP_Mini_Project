
public class FeedbackContext {

    public static String generateFeedbackId() {
        // Use timestamp to ensure unique IDs even after app restart
        return "FB-" + System.currentTimeMillis();
    }
}

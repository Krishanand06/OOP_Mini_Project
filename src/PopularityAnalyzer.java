
import java.util.List;
import java.util.concurrent.*;
import javax.swing.SwingUtilities;

/**
 * PopularityAnalyzer - Background service that analyzes menu item popularity
 * 
 * MULTITHREADING CONCEPTS DEMONSTRATED:
 * - ScheduledExecutorService for periodic task execution
 * - Runnable
 interface implementation via inner class
 * - Thread synchronization with synchronized keyword
 * - Inner classes (static and non-static)
 * - Thread-safe UI updates with SwingUtilities.invokeLater()
 */
public class PopularityAnalyzer {

    private ScheduledExecutorService executor;
    private volatile boolean running = false;
    private PopularityUpdateListener listener;
    private List<PopularItemData> latestData;

    /**
     * Inner class to hold popular item data
     * Demonstrates: Static nested class
     */
    public static class PopularItemData {
        private final int itemId;
        private final String itemName;
        private final String category;
        private final int totalOrdered;

        public PopularItemData(int itemId, String itemName, String category, int totalOrdered) {
            this.itemId = itemId;
            this.itemName = itemName;
            this.category = category;
            this.totalOrdered = totalOrdered;
        }

        public int getItemId() {
            return itemId;
        }

        public String getItemName() {
            return itemName;
        }

        public String getCategory() {
            return category;
        }

        public int getTotalOrdered() {
            return totalOrdered;
        }

        @Override
        public String toString() {
            return itemName + " (" + category + ") - " + totalOrdered + " orders";
        }
    }

    /**
     * Interface for receiving popularity updates
     * Demonstrates: Interface definition
     */
    public interface PopularityUpdateListener {
        void onPopularityUpdate(List<PopularItemData> popularItems);
    }

    /**
     * Inner class that performs the analysis task
     * Demonstrates: Non-static inner class implementing Runnable
     */
    private class AnalysisTask implements Runnable {
        @Override
        public void run() {
            try {
                // Query database for popular items
                List<Object[]> rawData = DatabaseManager.getPopularMenuItems(5);

                // Convert to PopularItemData objects
                List<PopularItemData> popularItems = new java.util.ArrayList<>();
                for (Object[] row : rawData) {
                    PopularItemData item = new PopularItemData(
                            (Integer) row[0], // item_id
                            (String) row[1], // item_name
                            (String) row[2], // category
                            (Integer) row[3] // total_ordered
                    );
                    popularItems.add(item);
                }

                // Update latest data (synchronized for thread safety)
                synchronized (PopularityAnalyzer.this) {
                    latestData = popularItems;
                }

                // Notify listener on Swing EDT (thread-safe UI update)
                if (listener != null && running) {
                    SwingUtilities.invokeLater(() -> {
                        listener.onPopularityUpdate(popularItems);
                    });
                }

            } catch (Exception e) {
                System.err.println("Error in popularity analysis: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Constructor
     */
    public PopularityAnalyzer() {
        this.executor = null;
        this.running = false;
        this.latestData = new java.util.ArrayList<>();
    }

    /**
     * Set the listener for popularity updates
     */
    public void setListener(PopularityUpdateListener listener) {
        this.listener = listener;
    }

    /**
     * Start the analyzer with specified update interval
     * 
     * @param intervalSeconds How often to analyze (in seconds)
     */
    public void start(int intervalSeconds) {
        if (running) {
            System.out.println("Analyzer is already running");
            return;
        }

        running = true;

        // Create ScheduledExecutorService with single thread
        executor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "PopularityAnalyzer-Thread");
            t.setDaemon(true); // Daemon thread won't prevent JVM shutdown
            return t;
        });

        // Schedule the analysis task to run periodically
        executor.scheduleAtFixedRate(
                new AnalysisTask(), // The task to run
                0, // Initial delay (run immediately)
                intervalSeconds, // Period between executions
                TimeUnit.SECONDS // Time unit
        );

        System.out.println("Popularity Analyzer started (updates every " + intervalSeconds + " seconds)");
    }

    /**
     * Stop the analyzer
     */
    public void stop() {
        if (!running) {
            return;
        }

        running = false;

        if (executor != null) {
            executor.shutdown(); // Graceful shutdown
            try {
                // Wait up to 5 seconds for tasks to complete
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    executor.shutdownNow(); // Force shutdown if needed
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("Popularity Analyzer stopped");
    }

    /**
     * Check if analyzer is running
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Get the latest popularity data (thread-safe)
     */
    public synchronized List<PopularItemData> getLatestData() {
        return new java.util.ArrayList<>(latestData);
    }

    /**
     * Manually trigger an analysis (useful for immediate update)
     */
    public void analyzeNow() {
        if (executor != null && !executor.isShutdown()) {
            executor.execute(new AnalysisTask());
        }
    }
}

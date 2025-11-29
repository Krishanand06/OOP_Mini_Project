import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    // Start with the main menu
                    MainMenuScreen mainMenu = new MainMenuScreen();
                    mainMenu.setVisible(true);
                } catch (Throwable t) {
                    t.printStackTrace();
                    JOptionPane.showMessageDialog(
                            null,
                            "Error starting application:\n" + t,
                            "Startup Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}

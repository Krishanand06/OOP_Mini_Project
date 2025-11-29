
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class UIUtils {

    /**
     * Adds a hover effect to a button.
     * Changes the background color when the mouse enters and exits.
     *
     * @param button   The button to add the effect to.
     * @param original The original background color.
     * @param hover    The hover background color.
     */
    public static void addHoverEffect(JButton button, Color original, Color hover) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(hover);
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                button.setBackground(original);
            }
        });
    }
}

package cs3560.a2;
import javax.swing.*;

public class MiniTwitter {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                AdminControlPanel controlPanel = AdminControlPanel.getInstance();
                controlPanel.display();
            }
        });
    }
}

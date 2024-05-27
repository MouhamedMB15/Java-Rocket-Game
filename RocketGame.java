import javax.swing.*;

/*
 * name - Mouhamed Mbengue 
 * school - University of ROchester 
 * date due - December 2nd 2023 
 * id - 32306354
 * email - mmbengue@u.rochester.edu
 */

//Main Class
public class RocketGame {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            // Default Set
            JFrame frame = new JFrame("Rocket Traveler Game");
            PanelGame gamePanel = new PanelGame();
            frame.add(gamePanel);
            frame.setSize(1900, 1050);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setVisible(true);
            frame.addKeyListener(new KeyHandler(gamePanel));
            frame.setFocusable(true);
            frame.requestFocus();
        });
    }
}

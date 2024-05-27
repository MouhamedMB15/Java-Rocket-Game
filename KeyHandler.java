
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    // Var
    private PanelGame panelGame;

    public KeyHandler(PanelGame panelGame) {
        this.panelGame = panelGame;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_SPACE) {
            panelGame.setSpaceKeyPressed(true);
            panelGame.shoot();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_SPACE) {
            panelGame.setSpaceKeyPressed(false);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}
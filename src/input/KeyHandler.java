package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    private boolean[] keys = new boolean[525];

    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }

    /**
     *
     * @param e
     */
    @Override
    public void keyTyped(KeyEvent e) {

    }

    public boolean[] getKeys() {
        return keys;
    }

}

package input;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.event.MouseInputListener;

public class MouseHandler implements MouseInputListener, MouseMotionListener, MouseWheelListener {

    public double zoomsens;

    public Point mouse = new Point();

    private boolean[] buttons = new boolean[10];

    public double scroll = 0;

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mousePressed(MouseEvent e) {
        buttons[e.getButton()] = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        buttons[e.getButton()] = false;
    }

    /**
     *
     * @param e
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        scroll += e.getPreciseWheelRotation() / 100;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouse = e.getLocationOnScreen();
    }

    /**
     *
     * @param e
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        mouse = e.getLocationOnScreen();
    }

    public boolean[] getButtons() {
        return buttons;
    }

}

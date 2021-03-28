package display;

import input.GameFileHandler;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import minesweeperv2.MinesweeperGrid;

public class Display extends TFrame {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public final int UPDATE_SPEED = 5;
    public int update = 0;
    
    private int loadTimer = 0;
    private static final int LOAD_TIME = 25;

    private double speed = 10;

    Point mouse = mousehandler.mouse;

    public boolean keys[] = keyhandler.getKeys();
    public int keyBuffer[] = new int[keys.length];
    public final int MOVE_BUFFER_TIME = 35;

    File file = new File("C:\\Users\\thaiboman\\Desktop\\Server ports\\PortA.sav");
    
    GameFileHandler gfh = new GameFileHandler();

    // Grid better work this time
    private static MinesweeperGrid board = new MinesweeperGrid(150, 250, 3000);

    private double x = -width / 2;

    public Display(int width, int height, int screen) {
        super(width, height, screen);
        loadBoard();
        panel.setGrid(board);
        init();
    }

    public void init() {
        setBufferTimes(150);
    }

    @Override
    public void close() {

    }

    @Override
    public void render() {
        panel.repaint();
    }

    @Override
    public void update() {
        if (board.hasWon()) {
            JOptionPane.showMessageDialog(this, "Congradulations! You won!!!", "You Won!", JOptionPane.PLAIN_MESSAGE);
            board.reset();
            saveChanges();
        }
        autoload();
    }
    
    public void autoload() {
        if (runningServer()) {
            if (loadTimer >= LOAD_TIME) {
            loadBoard();
            panel.setGrid(board);
            loadTimer = 0;
        } else {
            loadTimer++;
        }
        }
    }

    @Override
    public void handleKeys() {
        keys = keyhandler.getKeys();
        mousebuttons = mousehandler.getButtons();
        if (keyRegister(KeyEvent.VK_ESCAPE)) {
            togglePause();
        }
        if (keyRegister(KeyEvent.VK_A)) {
            panel.camera.xOffset += speed;
        }
        if (keyRegister(KeyEvent.VK_D)) {
            panel.camera.xOffset -= speed;
        }
        if (keyRegister(KeyEvent.VK_W)) {
            panel.camera.yOffset += speed;
        }
        if (keyRegister(KeyEvent.VK_S)) {
            panel.camera.yOffset -= speed;
        }

        mouse = getMouseGridRelative();
        panel.mouse = mouse;
        if (mouseRegister(1)) {
            clearTile(mouse.x, mouse.y);
            saveChanges();
        }
        if (mouseRegister(3)) {
            board.setMask(mouse.x, mouse.y, MinesweeperGrid.FLAGGED);
            saveChanges();
        }
    }

    public boolean runningServer() {
        return file.isFile();
    }
    
    public boolean saveChanges() {
        if (runningServer()) {
//            System.out.println("saving");
            return gfh.saveBoard(board, file.getAbsolutePath());
        } else return false;
    }
    
    public boolean loadBoard() {
        if (runningServer()) {
            board = gfh.loadBoard(file.getAbsolutePath());
            board.retally();
//            System.out.println("board loaded");
            return true;
        } else return false;
    }

    public void clearTile(int x, int y) {
        if (board.getOverlayNumber(x, y) == 0) {
            if (board.getMask(x, y) == MinesweeperGrid.COVERED) clearZeroTiles(x, y);
        } else if (board.getOverlayNumber(x, y) == 9) {
            board.reset();
        } else {
            board.setMask(x, y, MinesweeperGrid.UNCOVERED);
        }
    }

    public void clearZeroTiles(int x, int y) {
        LinkedList<Point> tiles = board.getLinkedZeros(x, y);
        Point p;
        for (int i = 0; i < tiles.size(); i++) {
            p = tiles.get(i);
            board.setMask(p.x, p.y, MinesweeperGrid.UNCOVERED);
        }
    }

    public Point getMouseGridRelative() {
        double x = getMousePosition().x / board.tileSize;
        double y = getMousePosition().y / board.tileSize;
        return new Point((int) x, (int) y);
    }

    public static int rand(int l, int h) {
        return (int) (Math.random() * ((h - l) + 1) + l);
    }

}

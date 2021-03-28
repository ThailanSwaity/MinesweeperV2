package display;

import input.KeyHandler;
import input.MouseHandler;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;

import javax.swing.JFrame;

public class TFrame extends JFrame implements Runnable {

    // Initial Nulls
    public static int width;
    public static int height;

    // Class Declarations
    protected TPanel panel;
    protected MouseHandler mousehandler = new MouseHandler();
    protected KeyHandler keyhandler = new KeyHandler();
    // TODO Create new audio handler capable of playing multiple audio files at once.
    
    // Experimental custom key register time idea
    protected static final int DEFAULT_KEY_BUFFER_TIME = 50;
    
    protected boolean[] keys = keyhandler.getKeys();
    protected int[] keyBufferTime = new int[keys.length];
    protected int[] keyBuffer = new int[keys.length];
    
    protected boolean[] mousebuttons = mousehandler.getButtons();
    protected int[] mouseTimes = new int[mousebuttons.length];

    // Initial Vals
    protected boolean isRunning = true;
    protected boolean paused = false;

    public int framesPerSecond = 0;
    public int framesDisplay = 0;

    public double amountOfTicks = 60.0;

    /**
     * Creates a runnable TFrame ready for use
     * @param width Width of the TFrame
     * @param height Height of the TFrame
     * @param screen Screen to display the TFrame on
     */
    public TFrame(int width, int height, int screen) {
        this.width = width;
        this.height = height;
        setSize(width, height);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        panel = new TPanel(width, height);
        this.add(panel);
        this.addMouseListener(mousehandler);
        this.addMouseMotionListener(mousehandler);
        this.addMouseWheelListener(mousehandler);
        this.addKeyListener(keyhandler);
        setBufferTimes(DEFAULT_KEY_BUFFER_TIME);
        showOnScreen(screen, this);
        setVisible(true);
    }

    /**
     * Creates a runnable TFrame ready for use
     * @param width Width of the TFrame
     * @param height Height of the TFrame
     */
    public TFrame(int width, int height) {
        this(width, height, 0);
    }
    
    public void setBufferTimes(int t) {
        for (int i = 0; i < keyBufferTime.length; i++) {
            keyBufferTime[i] = t;
        }
    }

    /**
     * Moves the frame to the specified screen
     * @param screen Screen to switch the display to
     * @param frame Frame to switch
     */
    public void showOnScreen(int screen, TFrame frame) {
        frame.setLocationRelativeTo(null);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gd = ge.getScreenDevices();
        if (screen > -1 && screen < gd.length) {
            frame.setLocation(gd[screen].getDefaultConfiguration().getBounds().x + (gd[screen].getDefaultConfiguration().getBounds().width / 2) - (frame.width / 2), frame.getY());
        } else if (gd.length > 0) {
            frame.setLocation(gd[0].getDefaultConfiguration().getBounds().x + (gd[screen].getDefaultConfiguration().getBounds().width / 2) - (frame.width / 2), frame.getY());
        } else {
            throw new RuntimeException("No screens found");
        }
    }

    /**
     * Displays the frame on the entire screen
     * @param frame 
     */
    public void fullScreen(TFrame frame) {
        frame.setVisible(false);
        frame.setExtendedState(TFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
        frame.setVisible(true);
    }

    /**
     * Displays the application on the entire screen
     */
    public void fullScreen() {
        this.setVisible(false);
        this.setExtendedState(TFrame.MAXIMIZED_BOTH);
        this.setUndecorated(true);
        this.setVisible(true);
    }
    
    /**
     * Registers a key press.
     * 
     * Key buffer times are modifiable using the keyBufferTime[] array
     * 
     * @param key KeyEvent.VK_(Key value)
     * @return boolean stating the actuation of the key (Action triggered or not)
     */
    public boolean keyRegister(int key) {
        if (keys[key]) {
            if (keyBuffer[key] == 0) {
                keyBuffer[key]++;
                return true;
            } else if (keyBuffer[key] >= keyBufferTime[key]) {
                keyBuffer[key] = 1;
                return true;
            }
            keyBuffer[key]++;
            return false;
        } else {
            keyBuffer[key] = 0;
            return false;
        }
    }
    
    public boolean mouseRegister(int button) {
        if (mousebuttons[button]) {
            if (mouseTimes[button] == 0) {
                mouseTimes[button] = 1;
                return true;
            } else {
                return false;
            }
        } else {
            mouseTimes[button] = 0;
            return false;
        }
    }
    
    @Override
    public Point getMousePosition() {
        double x = (((mousehandler.mouse.getX() - this.getX() - 8) / panel.camera.zoom) - panel.camera.xOffset);
        double y = (((mousehandler.mouse.getY() - this.getY() - 30) / panel.camera.zoom) - panel.camera.yOffset);
        return new Point((int)x, (int)y);
    }
    
    @Override
    public void run() {
        boolean running = true;
        
        final boolean RENDER_TIME = false;
        
        int UPS = 120, FPS = 60;
        long initialTime = System.nanoTime();
        final double timeU = 1000000000 / UPS;
        final double timeF = 1000000000 / FPS;
        double deltaU = 0, deltaF = 0;
        int frames = 0, ticks = 0;
        long timer = System.currentTimeMillis();
        
        while (running) {
            long currentTime = System.nanoTime();
            deltaU += (currentTime = initialTime) / timeU;
            deltaF += (currentTime = initialTime) / timeF;
            initialTime = currentTime;
            
            if (deltaU >= 1) {
                if (!paused) update();
                handleKeys();
                ticks++;
                deltaU--;
            }
            
            if (deltaF >= 1) {
                render();
                frames++;
                deltaF--;
            }
            
            if (System.currentTimeMillis() - timer > 1000) {
                if (RENDER_TIME) {
                    System.out.printf("UPS: %s, FPS: %s", ticks, frames);
                }
                frames = 0;
                ticks = 0;
                timer += 1000;
            }
        }
        close();
    }

    public void close() {
        // TODO Auto-generated method stub
    }

    public void render() {

    }

    public void handleKeys() {

    }

    public void update() {

    }

    /**
     * Pauses or un-pauses the process
     */
    public void togglePause() {
        paused = !paused;
    }

}

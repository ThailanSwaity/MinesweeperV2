package display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import minesweeperv2.MinesweeperGrid;

public class TPanel extends JPanel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    // Class declarations
    protected Camera2D camera = new Camera2D();

    private static MinesweeperGrid board;

    public Point mouse = new Point(0, 0);

    public int width;
    public int height;

    private static Color[] colours = {Color.WHITE, Color.CYAN, Color.BLUE, Color.MAGENTA, Color.PINK, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.RED, Color.BLACK};

    public TPanel(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void paintComponent(Graphics g) {
        g.clearRect(0, 0, width, height);
        try {
            drawGrid(g, board);
            g.drawString("MouseX: " + mouse.x + ", MouseY: " + mouse.y, 1100, 10);
            g.drawString("Tiles Cleared: " + board.getTilesCleared(), 1100, 20);
            g.drawString("Bombs: " + board.getnBombs(), 1100, 30);
        } catch (NullPointerException e) {
            System.out.println("Bullshit rendering");
        }

    }

    public void drawGrid(Graphics g, MinesweeperGrid board) {
        
        double gx = camera.xOffset + (board.getWidth() + 1) * board.tileSize;
        double gy = camera.yOffset + (board.getHeight() + 1) * board.tileSize;

        for (int i = 0; i < board.getHeight() + 1; i++) {
            double y = (i * board.tileSize + camera.yOffset);
            for (int j = 0; j < board.getWidth() + 1; j++) {
                double x = (j * board.tileSize + camera.xOffset);
                int n = board.getOverlayNumber(j, i);
                int m = board.getMask(j, i);
                if (m == MinesweeperGrid.UNCOVERED) {
                    g.setColor(colours[n]);
                    g.fillRect((int) x, (int) y, board.tileSize, board.tileSize);
                    g.setColor(Color.BLACK);
                    if (n != 0) {
                        g.drawString(n + "", (int) x + (board.tileSize / 2), (int) y + 10 + (board.tileSize / 2));
                    }
                } else if (m == MinesweeperGrid.COVERED) {
                    g.setColor(Color.LIGHT_GRAY);
                    g.fillRect((int) x, (int) y, board.tileSize, board.tileSize);
                    g.setColor(Color.BLACK);
                } else if (m == MinesweeperGrid.FLAGGED) {
                    g.setColor(Color.GREEN);
                    g.fillRect((int) x, (int) y, board.tileSize, board.tileSize);
                    g.setColor(Color.BLACK);
                    g.drawString("FLAGGED", (int) x + (board.tileSize / 15), (int) y + 10 + (board.tileSize / 2));
                }
            }
        }

        for (int i = 0; i < board.getWidth() + 2; i++) {
            double x = (i * board.tileSize + camera.xOffset);
            double y = (i * board.tileSize + camera.yOffset);
            g.drawLine((int) x, (int) camera.yOffset, (int) x, (int) gy);
            g.drawLine((int) camera.xOffset, (int) y, (int) gx, (int) y);
        }
    }

    public void drawPoly(Graphics g, Polygon poly) {
        double x;
        double y;
        double x1;
        double y1;
        for (int i = 0; i < poly.npoints - 1; i++) {
            if (i < poly.npoints - 2) {
                x = poly.xpoints[i] + camera.xOffset;
                y = poly.ypoints[i] + camera.yOffset;

                x1 = poly.xpoints[i + 1] + camera.xOffset;
                y1 = poly.ypoints[i + 1] + camera.yOffset;

            } else {
                x = poly.xpoints[i] + camera.xOffset;
                y = poly.ypoints[i] + camera.yOffset;

                x1 = poly.xpoints[i + 1] + camera.xOffset;
                y1 = poly.ypoints[i + 1] + camera.yOffset;
                g.drawLine((int) x, (int) y, (int) x1, (int) y1);

                x = poly.xpoints[poly.npoints - 1] + camera.xOffset;
                y = poly.ypoints[poly.npoints - 1] + camera.yOffset;

                x1 = poly.xpoints[0] + camera.xOffset;
                y1 = poly.ypoints[0] + camera.yOffset;
            }
            g.drawLine((int) x, (int) y, (int) x1, (int) y1);

        }
    }

    public void drawPoint(Graphics g, double x, double y, double x1, double y1) {
        g.drawLine((int) (x + camera.xOffset), (int) (y + camera.yOffset), (int) (x1 + camera.xOffset), (int) (y1 + camera.yOffset));
    }

    public void focusObject(Polygon object) {

    }

    public void initGrid() {

    }

    protected boolean objectInView(double x, double y, double r) {
        if (x > -camera.xOffset - r && x < (1920 / camera.zoom) - camera.xOffset + r) {
            if (y > -camera.yOffset - r && y < (1080 / camera.zoom) - camera.yOffset + r) {
                return true;
            }
        }
        return false;
    }

    protected boolean objectInView(double x, double y, double width, double height) {
        if (camera.xOffset <= (x + width) && (x + width) >= camera.xOffset - width) {
            if (camera.yOffset <= (y + height) && (y + height) >= camera.yOffset - height) {
                return true;
            }
        }
        return false;
    }

    public BufferedImage getScreenshot() {
        BufferedImage image = new BufferedImage(TFrame.width, TFrame.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        paintComponent(g2d);
        g2d.dispose();
        return image;
    }

    /**
     * Used for graphical testing means
     *
     * @param c
     * @param mult
     * @param g
     */
    // Cardioid style graph testing
    public void drawPolyCircleCardioid(PolyCircle c, double mult, Graphics g) {
        double con;
        double inc = 2 * Math.PI / c.npoints;
        double x;
        double y;
        double x1;
        double y1;
        for (int i = 0; i < c.npoints; i++) {
            con = (i + 1) * mult;
            x = c.radius * Math.cos(con * inc);
            y = c.radius * Math.sin(con * inc);
            x1 = c.radius * Math.cos((i + 1) * inc);
            y1 = c.radius * Math.sin((i + 1) * inc);
            drawPoint(g, x1, y1, x, y);
        }
    }

    public void drawPolyCircleCardioid(VectorShape c, double mult, Graphics g) {
        double con;
        double inc = 2 * Math.PI / c.npoints;
        double x;
        double y;
        double x1;
        double y1;
        for (int i = 0; i < c.npoints; i++) {
            con = (i + 1) * mult;
            x = c.getRadius() * Math.cos(con * inc) + c.getOrigin().x;
            y = c.getRadius() * Math.sin(con * inc) + c.getOrigin().y;
            x1 = c.getRadius() * Math.cos((i + 1) * inc) + c.getOrigin().x;
            y1 = c.getRadius() * Math.sin((i + 1) * inc) + c.getOrigin().y;
            drawPoint(g, x1, y1, x, y);
        }
    }

    public static int rand(int l, int h) {
        return (int) (Math.random() * ((h - l) + 1) + l);
    }

    public void setGrid(MinesweeperGrid board) {
        this.board = board;
    }

}

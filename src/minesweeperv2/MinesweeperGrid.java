/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweeperv2;

import java.awt.Point;
import java.io.Serializable;
import java.util.LinkedList;

/**
 *
 * @author thaiboman
 */
public class MinesweeperGrid implements Serializable {

    private int[][] overlay;
    private boolean[][] bombs;
    private int[][] mask;
    private int nbombs;

    public int tileSize = 25;

    public boolean lost = false;

    public static final int COVERED = 0;
    public static final int UNCOVERED = 1;
    public static final int FLAGGED = 2;

    private int width;
    private int height;

    public MinesweeperGrid(int width, int height, int bombs) {
        this.width = width;
        this.height = height;
        this.nbombs = bombs;
        this.overlay = new int[height][width];
        this.bombs = new boolean[height][width];
        this.mask = new int[height][width];
        init();
    }

    public static int rand(int l, int h) {
        return (int) (Math.random() * ((h - l) + 1) + l);
    }

    /**
     * Fuck you
     */
    public final void init() {
        setBombs();
        setOverlay();
        setMask(COVERED);
    }

    public void reset() {
        this.overlay = new int[height][width];
        this.bombs = new boolean[height][width];
        this.mask = new int[height][width];
        init();
    }

    public void setBombs(boolean[][] bombs) {
        this.bombs = bombs;
    }

    public void setBombs() {
        int x;
        int y;
        for (int i = 0; i < nbombs; i++) {
            do {
                x = rand(0, getWidth());
                y = rand(0, getHeight());
            } while (bombs[y][x]);
            setBomb(x, y);
        }
    }

    public int getnBombs() {
        return nbombs;
    }

    public boolean hasWon() {
        return (getTilesCleared() == ((getWidth() + 1) * (getHeight() + 1)) - nbombs);
    }

    public void setOverlay(int[][] overlay) {
        this.overlay = overlay;
    }
    
    public void retally() {
        int n = 0;
        width = bombs[0].length;
        height = bombs.length;
        for (int i = 0; i < getHeight() + 1; i++) {
            for (int j = 0; j < getWidth() + 1; j++) {
                if (isBomb(j, i)) n++;
            }
        }
        nbombs = n;
    }
    
    public void setOverlay() {
        for (int i = 0; i < getHeight() + 1; i++) {
            for (int j = 0; j < getWidth() + 1; j++) {
                if (bombs[i][j]) {
                    overlay[i][j] = 9;
                } else {
                    overlay[i][j] = neighbours(j, i);
                }
            }
        }
    }

    public void setMask(int state) {
        for (int i = 0; i < getHeight(); i++) {
            for (int j = 0; j < getWidth(); j++) {
                mask[i][j] = state;
            }
        }
    }

    public int getMask(int x, int y) {
        try {
            return mask[y][x];
        } catch (ArrayIndexOutOfBoundsException e) {
            return -1;
        }
    }

    public void setMask(int x, int y, int state) {
        try {
            mask[y][x] = state;
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }
    
    public void setMask(int[][] mask) {
        this.mask = mask;
    }

    public int getTilesCleared() {
        int n = 0;
        for (int i = 0; i < getHeight() + 1; i++) {
            for (int j = 0; j < getWidth() + 1; j++) {
                if (getMask(j, i) == MinesweeperGrid.UNCOVERED) {
                    n++;
                }
            }
        }
        return n;
    }

    public int neighbours(int x, int y) {
        int n = 0;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (i == 0 && j == 0) {

                } else {
                    if (isBomb(x + j, y + i)) {
                        n++;
                    }
                }
            }
        }
        return n;
    }

    /**
     * Finds linked zeros so that the tiles can be cleared all at once.
     *
     * @param x Starting tile x coordinate.
     * @param y Starting tile y coordinate.
     * @return A list of all coordinates belonging to empty tiles.
     */
    public LinkedList<Point> getLinkedZeros(int x, int y) {
        LinkedList<Point> inside = new LinkedList<>();
        LinkedList<Point> outside = new LinkedList<>();
        outside.add(new Point(x, y));
        Point p;
        Point p1;
        int n = 0;
        do {
            for (int i = 0; i < inside.size(); i++) {
                p = inside.get(i);
                outside.addAll(getOverlayNeighboursOfType(p.x, p.y, 0));
            }
            for (int i = 0; i < outside.size(); i++) {
                p1 = outside.get(i);
                if (!inside.contains(p1)) {
                    inside.add(p1);
                }
            }
            outside.clear();
            n++;
//            System.out.println(n);
        } while (n < (25));
        for (int i = 0; i < inside.size(); i++) {
            p = inside.get(i);
            outside.addAll(getOverlayNeighbours(p.x, p.y));
        }
        for (int i = 0; i < outside.size(); i++) {
            p1 = outside.get(i);
            if (!inside.contains(p1)) {
                inside.add(p1);
            }
        }
        outside.clear();
        return inside;
    }

    public LinkedList<Point> removeDuplicates(LinkedList<Point> a) {
        for (int i = 0; i < a.size(); i++) {
            for (int j = 0; j < a.size(); j++) {
                if (i != j) {
                    if (a.get(i) == a.get(j)) {
                        a.remove(j);
                    }
                }
            }
        }
        return a;
    }

    public LinkedList<Point> getOverlayNeighboursOfType(int x, int y, int type) {
        LinkedList<Point> points = new LinkedList<>();
        if (getOverlayNumber(x, y) != type) {
            return points;
        }
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (j == 0 && i == 0) {
                } else {
                    if (getOverlayNumber(x + j, y + i) == type) {
                        points.add(new Point(x + j, y + i));
                    }
                }
            }
        }
        return points;
    }

    public LinkedList<Point> getOverlayNeighbours(int x, int y) {
        LinkedList<Point> points = new LinkedList<>();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (j == 0 && i == 0) {
                } else {
                    if (getOverlayNumber(x + j, y + i) != -1) {
                        points.add(new Point(x + j, y + i));
                    }
                }
            }
        }
        return points;
    }

    public int getOverlayNumber(int x, int y) {
        try {
            return overlay[y][x];
        } catch (ArrayIndexOutOfBoundsException e) {
            return -1;
        }
    }

    public boolean isBomb(int x, int y) {
        try {
            return bombs[y][x] == true;
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    public int getWidth() {
        return width - 1;
    }

    public int getHeight() {
        return height - 1;
    }

    public void setBomb(int x, int y) {
        try {
            bombs[y][x] = true;
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }

    public int[][] getOverlay() {
        return overlay;
    }

    public boolean[][] getBombs() {
        return bombs;
    }

    public int[][] getMask() {
        return mask;
    }

}

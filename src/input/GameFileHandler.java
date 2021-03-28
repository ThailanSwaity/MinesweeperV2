/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package input;

import java.util.Arrays;
import java.util.LinkedList;
import minesweeperv2.MinesweeperGrid;

/**
 *
 * @author thaiboman
 */
public class GameFileHandler {

    FileHandler filehandler = new FileHandler();

    public GameFileHandler() {

    }

    public boolean saveBoard(MinesweeperGrid board, String filename) {
        clearFile(filename);
        saveMatrix(board.getOverlay(), filename);
//        System.out.println("LOADING LINE ZERO");
//        System.out.println(filehandler.loadLine(filename, 0));
//        System.out.println(" ");
        addBlankLine(filename);
        saveMatrix(board.getBombs(), filename);
        addBlankLine(filename);
        saveMatrix(board.getMask(), filename);
        addBlankLine(filename);
        
        return true;
    }

    public MinesweeperGrid loadBoard(String filename) {
//        System.out.println(filehandler.loadLine(filename, 0)); // Testing load line
//        System.out.println("^^^^^"); // Still part of the testing of load line
        return loadFileMatrix(filename);
    }

    public LinkedList<String[]> getFileMatrix(String filename) {
        LinkedList<String[]> arrays = new LinkedList<>();
        LinkedList<String> array = new LinkedList<>();
        for (int i = 0; i < filehandler.getLines(filename); i++) {
            if (" ".equals(filehandler.loadLine(filename, i))) {
                arrays.add(listToArray(array));
                array.clear();
            } else {
                array.add(filehandler.loadLine(filename, i));
            }
        }
        for (int i = 0; i < arrays.size(); i++) { // Even more testing stuff
//            System.out.println(Arrays.toString(arrays.get(i))); // What that part says below me!
//            System.out.println("Array " + i + " length: " + arrays.get(i).length);
        } // Just highlighting bug checks to make them easy to remove later
        return arrays;
    }

    public MinesweeperGrid loadFileMatrix(String filename) {
        MinesweeperGrid board = new MinesweeperGrid(25, 25, 100);
        LinkedList<String[]> mats = getFileMatrix(filename);
//        System.out.println("Checking lengths: ");
//        System.out.println(mats.get(0).length);
//        System.out.println(mats.get(1).length);
//        System.out.println(mats.get(2).length);
        try {
            String[][] mat = new String[mats.get(0).length][mats.get(0)[0].length()];
            String[] temp = mats.get(0);
//        System.out.println("Temp length: " + temp.length); // Showing length
        for (int i = 0; i < temp.length; i++) {
            for (int j = 0; j < temp[i].length(); j++) {
                mat[i][j] = temp[i].charAt(j) + "";
            }
        }
//        System.out.println("Array Loaded: "); // More stuff
//        System.out.println(matToString(mat)); // Showing that array has loaded properly
        board.setOverlay(matToInteger(mat));
        temp = mats.get(1);
//        System.out.println("Temp length: " + temp.length);
        for (int i = 0; i < temp.length; i++) {
            for (int j = 0; j < temp[i].length(); j++) {
                mat[i][j] = temp[i].charAt(j) + "";
            }
        }
//        System.out.println("Array Loaded: "); // More stuff
//        System.out.println(matToString(mat)); // Showing that array has loaded properly
        board.setBombs(matToBoolean(mat));
        temp = mats.get(2);
//        System.out.println("Temp length: " + temp.length);
        for (int i = 0; i < temp.length; i++) {
            for (int j = 0; j < temp[i].length(); j++) {
                mat[i][j] = temp[i].charAt(j) + "";
            }
        }
//        System.out.println("Array Loaded: "); // More stuff
//        System.out.println(matToString(mat)); // Showing that array has loaded properly
        board.setMask(matToInteger(mat));
        }
        catch (IndexOutOfBoundsException e) {
            System.out.println("Error loading");
            return loadFileMatrix(filename);
        }
        return board;
    }

    public boolean[][] matToBoolean(String[][] mat) {
        boolean[][] data = new boolean[mat.length][mat[0].length];
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[0].length; j++) {
                data[i][j] = "t".equals(mat[i][j]);
            }
        }
//        System.out.println("Prior lengths");
//        System.out.println(mat.length);
//        System.out.println(mat[0].length);
//        System.out.println("Save lengths");
//        System.out.println(data.length);
//        System.out.println(data[0].length);
        return data;
    }

    public int[][] matToInteger(String[][] mat) {
        int[][] data = new int[mat.length][mat[0].length];
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[0].length; j++) {
                data[i][j] = Integer.parseInt(mat[i][j]);
            }
        }
        return data;
    }

    public boolean clearFile(String filename) {
        return filehandler.save("", filename);
    }

    public void addBlankLine(String filename) {
        filehandler.append(" ", filename);
    }

    public boolean saveMatrix(int[][] mat, String filename) {
        String data = matToString(mat);
//        System.out.println(data);
        String[] lines = splitLines(data);
//        System.out.println(Arrays.toString(lines));
//        System.out.println("Saved Lines: ");
//        System.out.println("y: " + mat.length);
//        System.out.println("x: " + mat[0].length);
//        System.out.println("lines length: " + lines.length);
        return saveLines(lines, filename);
    }

    public boolean saveMatrix(boolean[][] mat, String filename) {
        String data = matToString(mat);
//        System.out.println(data);
        String[] lines = splitLines(data);
//        System.out.println(Arrays.toString(lines));
//        System.out.println("Saved Lines: ");
//        System.out.println("y: " + mat.length);
//        System.out.println("x: " + mat[0].length);
//        System.out.println("lines length: " + lines.length);
        return saveLines(lines, filename);
    }

    public boolean saveLines(String[] lines, String filename) {
        for (int i = 0; i < lines.length; i++) {
            filehandler.append(lines[i], filename);
        }
        return true;
    }

    public String[] splitLines(String data) {
        return data.split("\r\n|\r|\n");
    }

    public String[] listToArray(LinkedList<String> list) {
        String[] data = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            data[i] = list.get(i);
        }
        return data;
    }

    public String matToString(int[][] mat) {
        String data = "";
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[0].length; j++) {
                data += mat[i][j];
            }
            data += "\n";
        }
        return data;
    }
    
    public String matToString(String[][] mat) {
        String data = "";
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[0].length; j++) {
                data += mat[i][j];
            }
            data += "\n";
        }
        return data;
    }

    public String matToString(boolean[][] mat) {
        String data = "";
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[0].length; j++) {
                if (mat[i][j]) {
                    data += "t";
                } else {
                    data += "f";
                }
            }
            data += "\n";
        }
        return data;
    }

}

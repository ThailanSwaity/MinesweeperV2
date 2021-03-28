package input;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.LinkedList;

public class FileHandler<T> {

    public FileHandler() {

    }

    public String loadLine(String filename, int liner) {
        int lines = 0;
        String text;
        try {
            FileReader stream = new FileReader(filename);
            try (BufferedReader file = new BufferedReader(stream)) {
                String line = file.readLine();
                while (lines <= liner - 1) {
                    line = file.readLine();
                    lines++;
                }
                text = line;
                file.close();
                stream.close();
            }
            return text;
        } catch (NullPointerException | IOException e) {
            return null;
        }
    }

    public int getLines(String filename) {
        int lines = 0;
        try {
            FileReader stream = new FileReader(filename);
            try (BufferedReader file = new BufferedReader(stream)) {
                String line = file.readLine();
                while (line != null) {
                    line = file.readLine();
                    lines++;
                }
                file.close();
                stream.close();
            }
            return lines;
        } catch (NullPointerException | IOException e) {
            return -1;
        }
    }

    public boolean save(String text, String filename) {
        try {
            FileWriter stream = new FileWriter(filename);
            try (PrintWriter file = new PrintWriter(stream)) {
                file.print(text);
                file.close();
                stream.close();
            }
            return true;
        } catch (IOException | NullPointerException e) {
            return false;
        }
    }

    public boolean saveObject(T data, String filename) {
        try {
            FileOutputStream stream = new FileOutputStream(filename);
            try (ObjectOutputStream output = new ObjectOutputStream(stream)) {
                output.writeObject(data);
                output.close();
                stream.close();
            }
            return true;
        } catch (NullPointerException | IOException e) {
            return false;
        }
    }

    public T openObject(String filename) {
        try {
            FileInputStream stream = new FileInputStream(filename);
            T object;
            try (ObjectInputStream input = new ObjectInputStream(stream)) {
                object = (T) input.readObject();
                input.close();
                stream.close();
            }
            return object;
        } catch (ClassCastException | IOException | ClassNotFoundException e) {
            return null;
        }
    }

    public boolean append(String line, String filename) {
        try {
            FileWriter stream = new FileWriter(filename, true);
            try (PrintWriter file = new PrintWriter(stream)) {
                file.println(line);
                file.close();
                stream.close();
            }
            return true;
        } catch (IOException | NullPointerException e) {
            return false;
        }
    }

    public String[] openArray(String filename) {
        try {
            FileReader stream = new FileReader(filename);
            String[] array;
            try (BufferedReader file = new BufferedReader(stream)) {
                String line = file.readLine();
                LinkedList<String> list = new LinkedList<>();
                while (line != null) {
                    list.add(line);
                    line = file.readLine();
                }
                file.close();
                stream.close();
                array = new String[0];
                array = list.toArray(array);
            }
            return array;
        } catch (ArrayIndexOutOfBoundsException | NullPointerException | IOException e) {
            return null;
        }
    }

}

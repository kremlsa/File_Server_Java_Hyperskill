package server;

import java.util.ArrayList;

public class FileNames {
    public static ArrayList<String> filenames = new ArrayList<>();

    public void addFile(String filename) {
        if (!isExist(filename) && checkFileName(filename)) {
            filenames.add(filename);
            System.out.println("The file " + filename + " added successfully");
        } else {
            System.out.println("Cannot add the file " + filename);
        }
    }

    public void getFile(String filename) {
        if (!isExist(filename)) {
            System.out.println("The file " + filename + " not found");
        } else {
            System.out.println("The file " + filename + " was sent");
        }
    }

    public void deleteFile(String filename) {
        if (!isExist(filename)) {
            System.out.println("The file " + filename + " not found");
        } else {
            filenames.remove(filename);
            System.out.println("The file " + filename + " was deleted");
        }
    }

    public Boolean isExist(String filename) {
        return filenames.contains(filename);
    }

    public Boolean checkFileName(String filename) {
        return filename.matches("file(?:[1-9]|0[1-9]|10)$");
    }
}

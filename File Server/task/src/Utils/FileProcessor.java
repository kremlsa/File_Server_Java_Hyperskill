package Utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class FileProcessor {

    public static HashMap<String, String> filenames = new HashMap<String, String>();

    public void export() {
        BufferedReader reader = null;
        File file = new File("C:\\Java_lessons\\File Server\\File Server\\task\\src\\server\\data\\bd.txt");
        if (file.exists()) {
        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line = null;
        while (true) {
            try {
                if (!((line = reader.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (line.contains(";")) {
                String[] strings = line.split(";");
                filenames.put(strings[0], strings[1]);
            }
        }
        }
    }

    public static String generateId(String filename) {
        return String.valueOf(Math.abs(filename.hashCode()));
    }
    public static String getNameById(String id) {
        String filepath = "-1";
        if (filenames.containsKey(id)) filepath = filenames.get(id);
        return filepath;
    }

    public static byte[] readFileB(String filepath) {
        byte[] data;
        Path path = Paths.get(filepath);
        if (Files.exists(path) && !Files.isDirectory(path)) {
            try {
                data = Files.readAllBytes(path);
                return data;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        data = new byte[0];
        return data;
    }

    public static void writeFileB(String filepath, byte[] data) {
        try (FileOutputStream fos = new FileOutputStream(filepath)) {
            fos.write(data);
            System.out.println(filepath + "****");
            //fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String writeFileBId(String filepath, byte[] data) {
        String hash = generateId(filepath);
        filenames.put(hash, filepath);
        exportFileNames();
        writeFileB(filepath, data);
        return hash;
    }

    public static byte[] readFileBId(String id) {
        byte[] data;
        String filepath = filenames.get(id);
        Path path = Paths.get(filepath);
        if (Files.exists(path) && !Files.isDirectory(path)) {
            try {
                data = Files.readAllBytes(path);
                return data;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        data = new byte[0];
        return data;
    }

    public static void exportFileNames() {
        String res = "";
        String filepath = "C:\\Java_lessons\\File Server\\File Server\\task\\src\\server\\data\\bd.txt";
        for(Map.Entry<String, String> entry : filenames.entrySet()){
            res = res + entry.getKey() + ";" + entry.getValue() + "\n";
        }
        File file = new File(filepath);
        BufferedWriter bf = null;;
        try{
            bf = new BufferedWriter( new FileWriter(file));
            bf.write( res);
            bf.flush();
        }catch(IOException e){
            e.printStackTrace();
        }finally{

            try{
                bf.close();
            }catch(Exception e){}
        }
    }
}

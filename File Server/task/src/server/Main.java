package server;

import Utils.FileProcessor;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.io.*;
import java.net.*;

public class Main {
    public static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws IOException {


        boolean isRun = true;
        String address = "127.0.0.1";
        int port = 23456;

        ServerSocket server = new ServerSocket(port, 50, InetAddress.getByName(address));
        while (isRun) {
            //ServerSocket server = new ServerSocket(port, 50, InetAddress.getByName(address));
            Socket socket = server.accept();
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output  = new DataOutputStream(socket.getOutputStream());
            String query = input.readUTF();
            String response = "";
            String id = "";
            FileProcessor fp = new FileProcessor();
            fp.export();
            String filepath = "";

            if (query.equals("exit")) {
                //isRun = false;
                //input.close();
                //output.close();
                socket.close();
                server.close();
                //isRun = false;
               return;
            } else {
                switch (query.split(";")[0]) {
                    case "PUT":
                        System.out.println(query);
                        if (query.split(";").length > 1) {
                            filepath = "C:\\Java_lessons\\File Server\\File Server\\task\\src\\server\\data\\" + query.split(";")[1];
                        } else {
                            filepath = "C:\\Java_lessons\\File Server\\File Server\\task\\src\\server\\data\\" + String.valueOf(System.nanoTime());
                        }
                        File f = new File(filepath);
                        if(f.exists() && !f.isDirectory()) {
                            response = "403";
                        } else {
                            int length = input.readInt();
                            byte[] data = new byte[length];
                            input.readFully(data, 0, data.length);
                            System.out.println(filepath);
                            id = fp.writeFileBId(filepath, data);
                            response = "200" + " " + id;
                        }
                        output.writeUTF(response);
                        break;
                    case "GET":
                        if (query.split(";")[1].equals("n")) {
                            filepath = "C:\\Java_lessons\\File Server\\File Server\\task\\src\\server\\data\\" + query.split(";")[2];
                            f = new File(filepath);
                            if (f.exists() && !f.isDirectory()) {
                                response = "200";
                                Path path = Paths.get(filepath);
                                //byte[] data = Files.readAllBytes(path);
                                byte[] data = FileProcessor.readFileB(filepath);
                                output.writeUTF(response);
                                //output.writeUTF(response);
                                output.writeInt(data.length); // write length of the message
                                output.write(data);
                            } else {
                                response = "403";
                                output.writeUTF(response);
                            }
                        } else {
                            filepath = fp.getNameById(query.split(";")[2]);
                            f = new File(filepath);
                            if (f.exists() && !f.isDirectory()) {
                                response = "200";
                                Path path = Paths.get(filepath);
                                //byte[] data = Files.readAllBytes(path);
                                byte[] data = FileProcessor.readFileB(filepath);
                                output.writeUTF(response);
                                //output.writeUTF(response);
                                output.writeInt(data.length); // write length of the message
                                output.write(data);
                            } else {
                                response = "403";
                                output.writeUTF(response);
                            }
                        }

                        break;
                    case "DELETE":
                        if (query.split(";")[1].equals("n")) {
                            filepath = "C:\\Java_lessons\\File Server\\File Server\\task\\src\\server\\data\\" + query.split(";")[2];
                        } else {
                            filepath = fp.getNameById(query.split(";")[2]);
                        }
                        f = new File(filepath);
                        if (f.exists() && !f.isDirectory()) {
                            response = "200";
                            delete(filepath);
                        } else {
                            response = "403";
                        }
                        output.writeUTF(response);
                        break;
                }
                output.flush();
            }
            //socket.close();
            //server.close();

        }
    }

    public static String readFile(String filepath) {
        String res = "";
        try {
            File myFile = new File(filepath);
            FileReader fileReader = new FileReader(myFile);
            BufferedReader reader = new BufferedReader(fileReader);
            String line = null;
            while ((line = reader.readLine()) != null) {
                res += line;
            }
            reader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return res;
    }

    public static void delete(String filepath) {
        try {
            File file = new File(filepath);
            file.delete();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}

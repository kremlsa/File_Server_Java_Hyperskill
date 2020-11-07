package client2;

import Utils.FileProcessor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.io.*;
import java.net.*;

import static server.Main.readFile;

public class Main {
    public static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        String address = "127.0.0.1";
        int port = 23456;
        Socket socket = new Socket(InetAddress.getByName(address), port);
        DataInputStream input = new DataInputStream(socket.getInputStream());
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        //DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
        byte[] data;
        String req = "";
        String response = "";
        String filename = "";
        System.out.println("Enter action ((1 - get the file, 2 - save the file, 3 - delete the file): ");
        String operation = sc.nextLine();
        switch (operation) {
            case "1":
                req = getRequest();
                output.writeUTF(req);
                System.out.println("The request was sent.");
                response = input.readUTF();
                if (processResponse(response, operation)) {
                    int length = input.readInt();
                    data = new byte[length];
                    input.readFully(data, 0, data.length);
                    filename = "C:\\Java_lessons\\File Server\\File Server\\task\\src\\client\\data\\" + sc.nextLine();
                    FileProcessor.writeFileB(filename, data);
                    System.out.print("File saved on the hard drive!");
                }
                break;
            case "2":
                System.out.print("Enter filename: ");
                String name = sc.nextLine();
                Path path = Paths.get("C:\\Java_lessons\\File Server\\File Server\\task\\src\\client\\data\\" + name);
                data = Files.readAllBytes(path);
                System.out.print("Enter name of the file to be saved on server: ");
                String outName = sc.nextLine();
                output.writeUTF("PUT" + ";" + outName);
                output.writeInt(data.length); // write length of the message
                output.write(data);
                response = input.readUTF();
                processResponse(response, operation);
                break;
            case "3":
                req = deleteRequest();
                output.writeUTF(req);
                System.out.println("The request was sent.");
                response = input.readUTF();
                processResponse(response, operation);
                break;
            case "exit":
                output.writeUTF("exit");
                output.flush();
                output.close();
                input.close();
                socket.close();
                System.exit(0);
                break;
        }
    }

    public static String getRequest() {
        String name = "";
        String mode = "";
        System.out.println("Do you want to get the file by name or by id (1 - name, 2 - id): ");
        String choice = sc.nextLine();
        switch (choice) {
            case "1":
                System.out.print("Enter filename: ");
                name = sc.nextLine();
                mode = "n";
                break;
            case "2":
                System.out.print("Enter id: ");
                name = sc.nextLine();
                mode = "i";
                break;
        }
        return "GET" + ";" + mode + ";" + name;
    }

    public static String deleteRequest() {
        String name = "";
        String mode = "";
        System.out.println("Do you want to delete the file by name or by id (1 - name, 2 - id): ");
        String choice = sc.nextLine();
        switch (choice) {
            case "1":
                System.out.print("Enter filename: ");
                name = sc.nextLine();
                mode = "n";
                break;
            case "2":
                System.out.print("Enter id: ");
                name = sc.nextLine();
                mode = "i";
                break;
        }
        return "DELETE" + ";" + mode + ";" + name;
    }

    public static Boolean processResponse(String response, String operation) {
        switch (operation) {
            case "2":
                if (response.startsWith("200")) {
                    System.out.println("Response says that file is saved! ID = " + response.substring(4));
                    return true;
                }
                else {
                    System.out.println("The response says that creating the file was forbidden!");
                }
                break;
            case "1":
                if (response.equals("200"))  {
                    System.out.print("The file was downloaded! Specify a name for it: ");
                    return true;
                }
                else {
                    System.out.println("The response says that this file is not found!");
                }
                break;
            case "3":
                if (response.equals("200")) {
                    System.out.println("The response says that the file was successfully deleted!");
                    return true;
                }
                else {
                    System.out.println("The response says that this file is not found!");
                }
                break;
        }
        return false;
    }

}

package myClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.security.MessageDigest;
import java.util.Scanner;


public class myClient {

    public static void main(String args[]) {
        try {
            String serverIp = "127.0.0.1";  // local ip
            String name = "";
            String id = "";
            String password = "";
            // create socket and request connection
            Socket socket = new Socket(serverIp, 7777);

            // TODO: implement user profile with database
            // TODO: implement sign in/up
            // use user input temporarily
            System.out.println("welcome to chat program!");

            while (true) {
                System.out.println("please enter your command");
                System.out.println("1. sign in");
                System.out.println("2. sign up");
                System.out.println("0. exit");

                Scanner scanner = new Scanner(System.in);
                while (!scanner.hasNextInt()) {
                    scanner.next();
                    System.out.println("command error! please enter 0~2");
                }

                int menu = scanner.nextInt();

                if (menu == 1) {
                    // TODO: sign in
                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataOutputStream.writeUTF("in");
                    scanner.nextLine();
                    System.out.println("please enter your id");
                    id = scanner.nextLine();
                    System.out.println("please enter your password");
                    password = getSHA256(scanner.nextLine());   // apply encryption
                    // TODO: authentication by server
                    dataOutputStream.writeUTF(id);
                    dataOutputStream.writeUTF(password);

                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                    String result = dataInputStream.readUTF();
                    if(result.equals("success")){
                        break;
                    }
                } else if (menu == 2) {
                    // TODO: sign up
                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataOutputStream.writeUTF("up");
                    scanner = new Scanner(System.in);
                    System.out.println("what is your name?");
                    name = scanner.nextLine();
                    System.out.println("please enter your id");
                    id = scanner.nextLine();
                    System.out.println("please enter your password");
                    password = getSHA256(scanner.nextLine());   // apply encryption

                    dataOutputStream.writeUTF(name);
                    dataOutputStream.writeUTF(id);
                    dataOutputStream.writeUTF(password);

                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                    String result = dataInputStream.readUTF();

                    if(result.equals("success")){
                        break;
                    }
                } else if (menu == 0) {
                    // exit program
                    return;
                }
            }

            // TODO: get name by id in database
            System.out.println("connected to server");
            Thread sender = new Thread(new ClientSender(socket, name));
            Thread receiver = new Thread(new ClientReceiver(socket));
            receiver.start();
            sender.start();
        } catch (ConnectException ce) {
            ce.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class ClientSender extends Thread {
        Socket socket;
        DataOutputStream dataOutputStream;
        String name;

        ClientSender(Socket socket, String name) {
            this.socket = socket;
            try {
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                this.name = name;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void run() {
            Scanner scanner = new Scanner(System.in);
            try {
                if (dataOutputStream != null) {
                    dataOutputStream.writeUTF(name);
                }
                while (dataOutputStream != null) {
                    dataOutputStream.writeUTF("[" + name + "]" + scanner.nextLine());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static class ClientReceiver extends Thread {
        Socket socket;
        DataInputStream dataInputStream;

        ClientReceiver(Socket socket) {
            this.socket = socket;
            try {
                dataInputStream = new DataInputStream(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            while (dataInputStream != null) {
                try {
                    System.out.println(dataInputStream.readUTF());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getSHA256(String string) throws Exception {
        StringBuffer sb = new StringBuffer();

        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(string.getBytes());

        byte[] messageString = messageDigest.digest();
        for (int i = 0; i < messageString.length; i++) {
            byte tempByte = messageString[i];
            String tmp = Integer.toString((tempByte & 0xff) + 0x100, 16).substring(1);
            sb.append(tmp);
        }
        return sb.toString();
    }

}

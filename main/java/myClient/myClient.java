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
        myClientFrame frame;    // GUI object
        try {
            // String serverIp = "127.0.0.1";   // local ip
            String serverIp = "121.137.97.164"; // public ip
            String name = "";
            String id = "";
            String password = "";
            String status = "";

            // create socket and request connection
            Socket socket = new Socket(serverIp, 7777);

            System.out.println("===== welcome to chat program! =====");
            while (true) {
                // console UI
                System.out.println("===== please enter your command ====");
                System.out.println("============ 1. sign in ============");
                System.out.println("============ 2. sign up ============");
                System.out.println("============ 0. exit ===============");

                Scanner scanner = new Scanner(System.in);
                while (!scanner.hasNextInt()) {
                    scanner.next();
                    System.out.println("command error! please enter 0~2");
                }

                int menu = scanner.nextInt();

                if (menu == 1) {
                    // sign in
                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataOutputStream.writeUTF("in");
                    scanner.nextLine();
                    System.out.println("please enter your id");
                    id = scanner.nextLine();
                    System.out.println("please enter your password");
                    password = getSHA256(scanner.nextLine());   // apply encryption

                    // authentication by server
                    dataOutputStream.writeUTF(id);
                    dataOutputStream.writeUTF(password);

                    // get result data
                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                    String result = dataInputStream.readUTF();
                    if(result.equals("success")){
                        name = dataInputStream.readUTF();
                        status = "online";
                        break;
                    }else if(result.equals("failure")){
                        System.out.println("sign in fail! please try again.");
                    }
                } else if (menu == 2) {
                    // sign up
                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataOutputStream.writeUTF("up");
                    scanner = new Scanner(System.in);
                    System.out.println("what is your name?");
                    name = scanner.nextLine();
                    System.out.println("please enter your id");
                    id = scanner.nextLine();
                    System.out.println("please enter your password");
                    password = getSHA256(scanner.nextLine());   // apply encryption

                    // send data to server
                    dataOutputStream.writeUTF(name);
                    dataOutputStream.writeUTF(id);
                    dataOutputStream.writeUTF(password);

                    // get result data
                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                    String result = dataInputStream.readUTF();

                    if(result.equals("success")){
                        status = "online";
                        break;
                    }else if(result.equals("failure")){
                        System.out.println("sign up fail! please try again.");
                    }
                } else if (menu == 0) {
                    // exit program
                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataOutputStream.writeUTF("/exit");
                    socket.close();
                    System.exit(0);
                }
            }

            // open new frame
            frame = new myClientFrame(socket, name);
            frame.getLocationOnScreen();
            frame.statusLabel.setText(status);

            // run receiver and sender
            new ClientReceiver(socket, frame).start();
            ClientSender sender = new ClientSender(frame, name);
            sender.start();
        } catch (ConnectException ce) {
            ce.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class ClientSender extends Thread {
        Socket socket;
        myClientFrame frame;
        DataOutputStream dataOutputStream;
        String name;

        // initialize constructor
        public ClientSender(myClientFrame frame, String name) {
            this.frame = frame;
            this.socket = this.frame.socket;
            this.name = name;
            try {
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // send message to server
        public void sendMessage(){
            try {
                dataOutputStream.writeUTF("[" + name + "]" + frame.messageField.getText());
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    static class ClientReceiver extends Thread {
        Socket socket;
        myClientFrame frame;
        DataInputStream dataInputStream;

        // initialize constructor
        public ClientReceiver(Socket socket, myClientFrame frame) {
            this.socket = socket;
            this.frame = frame;
            try {
                this.dataInputStream = new DataInputStream(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // get message from server
        public void run() {
            while (dataInputStream != null) {
                try {
                    String message =  dataInputStream.readUTF();
                    if(message == null){
                        break;
                    }
                    frame.messageArea.append(message+"\n");
                    // scroll to bottom
                    frame.messageArea.setCaretPosition(frame.messageArea.getDocument().getLength());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // SHA-256 encryption for password
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
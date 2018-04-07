import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;


public class myClient {

    public static void main(String args[]){
        try{
            String serverIp = "127.0.0.1";

            // create socket and request connection
            Socket socket = new Socket(serverIp, 7777);

            // TODO: implement user profile with database
            // use user input temporarily
            System.out.println("what is your name?");
            Scanner scanner = new Scanner(System.in);

            Thread sender = new Thread(new ClientSender(socket, scanner.nextLine()));
            System.out.println("connected to server");
            Thread receiver = new Thread(new ClientReceiver(socket));
            sender.start();
            receiver.start();
        }catch(ConnectException ce){
            ce.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    static class ClientSender extends Thread{
        Socket socket;
        DataOutputStream dataOutputStream;
        String name;
        ClientSender(Socket socket, String name){
            this.socket = socket;
            try{
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                this.name = name;
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        public void run(){
            Scanner scanner = new Scanner(System.in);
            try{
                if(dataOutputStream != null){
                    dataOutputStream.writeUTF(name);
                }
                while(dataOutputStream != null){
                    dataOutputStream.writeUTF("[" + name + "]" + scanner.nextLine());
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    static class ClientReceiver extends Thread{
        Socket socket;
        DataInputStream dataInputStream;
        ClientReceiver(Socket socket){
            this.socket = socket;
            try{
                dataInputStream = new DataInputStream(socket.getInputStream());
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        public void run(){
            while(dataInputStream != null){
                try{
                    System.out.println(dataInputStream.readUTF());
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }

    }

}

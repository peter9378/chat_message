import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

public class myServer {
    HashMap clients;
    myServer(){
        clients = new HashMap();
        Collections.synchronizedMap(clients);
    }

    public void start(){
        ServerSocket serverSocket = null;
        Socket socket = null;
        try{
            serverSocket = new ServerSocket(7777);
            System.out.println("server is running now");
            while(true){
                socket = serverSocket.accept();
                System.out.println("[" + socket.getInetAddress() + ":"
                        + socket.getPort() + "]" + "has new connetcion");
                ServerReceiver thread = new ServerReceiver(socket);
                thread.start();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    void sendToAll(String msg){
        Iterator iter = clients.keySet().iterator();
        while(iter.hasNext()){
            try{
                DataOutputStream dataOutputStream = (DataOutputStream) clients.get(iter.next());
                dataOutputStream.writeUTF(msg);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void main(String args[]) {
        new myServer().start();
    }

    class ServerReceiver extends Thread{
        Socket socket;
        DataInputStream dataInputStream;
        DataOutputStream dataOutputStream;
        ServerReceiver(Socket socket){
            this.socket = socket;
            try{
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        public void run(){
            String name = "";
            try{
                name = dataInputStream.readUTF();
                sendToAll("[NOTICE] " + name + " has connected");
                clients.put(name, dataOutputStream);
                System.out.println("[NOTICE] current client : " + clients.size());
                while(dataInputStream != null){
                    sendToAll(dataInputStream.readUTF());
                }
            }catch(IOException e){
                e.printStackTrace();
            }finally{
                sendToAll("[NOTICE] " + name + " has disconnected");
                clients.remove(name);
                System.out.println("[" + socket.getInetAddress()
                        + ":" + socket.getPort() + "] has disconnected");
                System.out.println("current client : " + clients.size());
            }
        }
    }


    static String getTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("[hh:mm:ss] ");
        return sdf.format(new Date());
    }
}

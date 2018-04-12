import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;

public class myServer {
    enum Status {online, offline, busy};

    /*
    static class Client{
        Status status;
        String name;
        String id;
        String password;

        Client(String name, String id, String password){
            this.name = name;
            this.id = id;
            this.password = password;
            this.status = Status.online;
        }
    }
    */

    HashMap clients;
    myServer(){
        clients = new HashMap<String, Status>();
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
                        + socket.getPort() + "]" + "has new connection");
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
                DataOutputStream dataOutputStream = (DataOutputStream)clients.get(iter.next());
                dataOutputStream.writeUTF(msg);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

    }

    /*
     * send message to specific target
     * @param target target client user
     * @param msg message that want to send
     */
//    void sendToTarget(String target, String msg){
//        try{
//            DataOutputStream dataOutputStream = (DataOutputStream) clients.get(target);
//            dataOutputStream.writeUTF(msg);
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//    }

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
            // Client client;
            String name = "";
            String id = "";
            String password = "";
            String temp = "";
            try{
                String command = dataInputStream.readUTF();
                // TODO : check id and password in database
                if(command.equals("in")){
                    // sign in process
                    id = dataInputStream.readUTF();
                    password = dataInputStream.readUTF();

                    // check id and password in database

                }else if(command.equals("up")){
                    // sign up process
                    name = dataInputStream.readUTF();
                    id = dataInputStream.readUTF();
                    password = dataInputStream.readUTF();

                    // check if id is overlapped or not

                    // if all process works well

                }

            }catch(IOException e){
                e.printStackTrace();
            }

            try{
                sendToAll("[NOTICE] " + name + " has connected");
                clients.put(name, dataOutputStream);
                System.out.println("[NOTICE] current client : " + clients.size());
                while(dataInputStream != null){
//                    String target = dataInputStream.readUTF();
//                    sendToTarget(target, dataInputStream.readUTF());
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

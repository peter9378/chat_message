package myServer;

import user.User;
import user.UserDAO;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;

public class myServer {
    enum Status {online, offline, busy};

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
            while(true) {
                UserDAO userDAO = new UserDAO();
                try {
                    String command = dataInputStream.readUTF();
                    // TODO : check id and password in database
                    if (command.equals("in")) {
                        // sign in process
                        id = dataInputStream.readUTF();
                        password = dataInputStream.readUTF();

                        // check id and password in database
                        if(userDAO.isCorrectUser(id, password)){
                            // sign in success
                            System.out.println("sign in success");
                            break;
                        }else{
                            // TODO: classify sign in failure(password incorrect, id doesn't exist etc...)
                            // wrong password, sign in failure
                            System.out.println("password incorrect");
                        }
                    } else if (command.equals("up")) {
                        // sign up process
                        name = dataInputStream.readUTF();
                        id = dataInputStream.readUTF();
                        password = dataInputStream.readUTF();

                        // check if id is overlapped or not
                        if (userDAO.isIdExist(id)) {
                            // new User's index is -1
                            userDAO.addUser(new User(id, name, password, -1));
                            System.out.println("sign up success");
                            break;
                        } else {
                            // id overlap exception
                            System.out.println("already exist id");
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try{
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

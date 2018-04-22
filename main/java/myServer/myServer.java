package myServer;

import message.Message;
import message.MessageDAO;
import user.User;
import user.UserDAO;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

public class myServer {
    HashMap clients;
    UserDAO userDAO = new UserDAO();
    MessageDAO messageDAO = new MessageDAO();

    myServer() {
        clients = new HashMap();
        try {
            messageDAO.getConnection();
        }catch(SQLException e){
            e.printStackTrace();
        }
        Collections.synchronizedMap(clients);
    }

    public void start() {
        ServerSocket serverSocket = null;
        Socket socket = null;
        try {
            serverSocket = new ServerSocket(7777);
            System.out.println("server is running now");
            while (true) {
                socket = serverSocket.accept();
                System.out.println("[" + socket.getInetAddress() + ":"
                        + socket.getPort() + "]" + "has new connection");
                ServerReceiver thread = new ServerReceiver(socket);
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void sendToAll(String msg) {
        // TODO: send to only online user
        // TODO: when send successfully, update user's index(index++)
        // TODO: add message to database
        // TODO: send message include timestamp
        if(!msg.contains("[NOTICE]")) {
            messageDAO.addMessage(new Message(messageDAO.getIndex(), msg, getTimestamp()));
        }
        Iterator iter = clients.keySet().iterator();
        while (iter.hasNext()) {
            String name = (String)iter.next();
            if(userDAO.isOnlineUserByName(name)) {
                try {
                    DataOutputStream dataOutputStream = (DataOutputStream) clients.get(name);
                    dataOutputStream.writeUTF(getHMS(getTimestamp().toString()) + msg);
                    if(!msg.contains("[NOTICE]")) {
                        userDAO.setUserIndexByName(messageDAO.getIndex(), name);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String args[]) {
        new myServer().start();
    }

    class ServerReceiver extends Thread {
        Socket socket;
        DataInputStream dataInputStream;
        DataOutputStream dataOutputStream;

        ServerReceiver(Socket socket) {
            this.socket = socket;
            try {
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            // Client client;
            String name = "";
            String id = "";
            String password = "";

            while (true) {
                UserDAO userDAO = new UserDAO();
                try {
                    String command = dataInputStream.readUTF();
                    // TODO : check id and password in database
                    if (command.equals("in")) {
                        // sign in process
                        id = dataInputStream.readUTF();
                        password = dataInputStream.readUTF();

                        // check id and password in database
                        if (userDAO.isCorrectUser(id, password)) {
                            // sign in success
                            System.out.println("sign in success");
                            name = userDAO.getNameById(id);

                            // add online client
                            clients.put(name, dataOutputStream);
                            userDAO.updateUserStatus(id, "online");

                            dataOutputStream.writeUTF("success");
                            dataOutputStream.writeUTF(name);

                            sendUnreadMessage(id);

                            break;
                        } else {
                            // TODO: classify sign in failure(password incorrect, id doesn't exist etc...)
                            // wrong password, sign in failure
                            System.out.println("password incorrect");
                            dataOutputStream.writeUTF("failure");
                            break;
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

                            // add online client
                            clients.put(name, dataOutputStream);
                            userDAO.updateUserStatus(id, "online");

                            dataOutputStream.writeUTF("success");
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

            try {
                sendToAll("[NOTICE] " + name + " has joined");

                System.out.println("[NOTICE] current client : " + clients.size());
                while (dataInputStream != null) {
                    String text = dataInputStream.readUTF();
                    if(text.contains("/online")){
                        userDAO.updateUserStatus(id, "online");
                        sendUnreadMessage(id);
                    }else if(text.contains("/offline")){
                        userDAO.updateUserStatus(id, "offline");
                    }else if(text.contains("/busy")){
                        userDAO.updateUserStatus(id, "busy");
                    }else {
                        sendToAll(text);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                sendToAll("[NOTICE] " + name + " has disconnected");
                clients.remove(name);
                userDAO.updateUserStatus(id, "offline");
                System.out.println("[" + socket.getInetAddress()
                        + ":" + socket.getPort() + "] has disconnected");
                System.out.println("current client : " + clients.size());
            }
        }

        public void sendUnreadMessage(String id){
            LinkedList<Message> list = new LinkedList<Message>();
            list = messageDAO.getUnreadMessage(userDAO.getIndexById(id));
            for(Message message:list){
                try {
                    dataOutputStream.writeUTF(getHMS(getTimestamp().toString()) + message.getText());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getHMS(String timestamp){
        return "[" + timestamp.substring(11, 19) + "]";
    }

    static Timestamp getTimestamp() {
        Timestamp time = new Timestamp(System.currentTimeMillis());
        return time;
    }
}

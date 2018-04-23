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
    HashMap clients;    // clients socket hashmap
    UserDAO userDAO = new UserDAO();    // user database access object
    MessageDAO messageDAO = new MessageDAO();   // message database access object

    // default constructor
    myServer() {
        clients = new HashMap();
        try {
            // connect to message box database
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
                // make connect as thread(multi thread)
                ServerReceiver thread = new ServerReceiver(socket);
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // send message to all users
    void sendToAll(String msg) {
        // all message is stored in database without NOTICE message
        if(!msg.contains("[NOTICE]")) {
            messageDAO.addMessage(new Message(messageDAO.getSize(), msg, getTimestamp()));
        }

        Iterator iter = clients.keySet().iterator();
        while (iter.hasNext()) {
            String name = (String)iter.next();
            // send message to only online users
            if(userDAO.getStatusByName(name).equals("online")) {
                try {
                    DataOutputStream dataOutputStream = (DataOutputStream) clients.get(name);
                    dataOutputStream.writeUTF(getHMS(getTimestamp().toString()) + msg);
                    // update user's index
                    if(!msg.contains("[NOTICE]")) {
                        userDAO.setUserIndexByName(messageDAO.getSize(), name);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // main function
    public static void main(String args[]) {
        new myServer().start();
    }


    class ServerReceiver extends Thread {
        Socket socket;
        DataInputStream dataInputStream;
        DataOutputStream dataOutputStream;

        // default constructor
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
            String name = "";
            String id = "";
            String password = "";

            while (true) {
                UserDAO userDAO = new UserDAO();
                try {
                    String command = dataInputStream.readUTF();
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

                            // send sign in success message
                            dataOutputStream.writeUTF("success");
                            dataOutputStream.writeUTF(name);

                            // send unread message
                            sendUnreadMessage(id);
                            break;
                        } else {
                            // wrong password, sign in failure
                            System.out.println("password incorrect");
                            dataOutputStream.writeUTF("failure");
                            continue;
                        }
                    } else if (command.equals("up")) {
                        // sign up process
                        name = dataInputStream.readUTF();
                        id = dataInputStream.readUTF();
                        password = dataInputStream.readUTF();

                        // check if id or name is overlapped or not
                        if (userDAO.isIdExist(id)) {
                            System.out.println("already exist id");
                            dataOutputStream.writeUTF("failure-id");
                            continue;
                        }else if(userDAO.isNameExist(name)){
                            System.out.println("already exist name");
                            dataOutputStream.writeUTF("failure-name");
                            continue;
                        }else {
                            // new User's index is the number of current message
                            userDAO.addUser(new User(id, name, password, "online", messageDAO.getSize()));
                            System.out.println("sign up success");

                            // add online client
                            clients.put(name, dataOutputStream);

                            // send sign up success message
                            dataOutputStream.writeUTF("success");
                            break;
                        }
                    }else if(command.equals("/exit")){
                        this.socket.close();
                        dataInputStream.close();
                        break;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            try {
                sendToAll("[NOTICE] " + name + " has joined");
                System.out.println("[NOTICE] current client : " + clients.size());

                while (dataInputStream != null) {
                    String text = dataInputStream.readUTF();

                    // special commands check
                    if(text.contains("/online")){
                        userDAO.updateUserStatus(id, "online");
                        sendUnreadMessage(id);
                    }else if(text.contains("/offline")){
                        userDAO.updateUserStatus(id, "offline");;
                    }else if(text.contains("/busy")){
                        userDAO.updateUserStatus(id, "busy");
                    }else if(text.contains("/userlist")) {
                        LinkedList<User> users = userDAO.getUserList();
                        dataOutputStream.writeUTF("===== Userlist =====");
                        for(User user:users){
                            dataOutputStream.writeUTF(user.getName() + " - " + user.getStatus());
                        }
                    }else if(text.contains("/retrieve")) {
                        LinkedList<Message> messages = messageDAO.getAllMessages();
                        for(Message message:messages){
                            dataOutputStream.writeUTF(getHMS(message.getTimestamp().toString()) + message.getText());
                        }
                    }else {
                        if(userDAO.getStatusByName(name).equals("offline")){
                            dataOutputStream.writeUTF("you can't send message because your status is offline now.");
                        }else {
                            sendToAll(text);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // when client has disconnected
                sendToAll("[NOTICE] " + name + " has disconnected");
                clients.remove(name);
                userDAO.updateUserStatus(id, "offline");
                System.out.println("[" + socket.getInetAddress()
                        + ":" + socket.getPort() + "] has disconnected");
                System.out.println("current client : " + clients.size());
            }
        }

        // retrieve unread message by index
        public void sendUnreadMessage(String id){
            LinkedList<Message> list = new LinkedList<Message>();
            list = messageDAO.getUnreadMessage(userDAO.getIndexById(id));

            for(Message message:list){
                try {
                    dataOutputStream.writeUTF(getHMS(message.getTimestamp().toString()) + message.getText());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            userDAO.setUserIndexById(messageDAO.getSize(), id);
        }
    }

    // return timestamp to HMS
    public String getHMS(String timestamp){
        return "[" + timestamp.substring(11, 19) + "]";
    }

    // return current timestamp
    static Timestamp getTimestamp() {
        Timestamp time = new Timestamp(System.currentTimeMillis());
        return time;
    }
}

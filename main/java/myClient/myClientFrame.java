package myClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.MessageDigest;
import java.util.Scanner;

import myClient.myClient.*;

class WelcomeScreen extends JFrame{
    static JTextField jTextField = new JTextField(20);
    JButton signInBtn = new JButton("sign in");
    JButton signUpBtn = new JButton("sign up");

    Scanner scanner = new Scanner(System.in);
    static String id = "";
    String password = "";
    static String name = "";

    public WelcomeScreen(){

    }

    public WelcomeScreen(final Socket socket){
        super("Chat Messenger Program");
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

            setLayout(new FlowLayout());
            add(new JLabel("sign in"));
            add(signInBtn);
            add(new JLabel("sign up"));
            add(signUpBtn);
            add(new JLabel("exit"));


            setBounds(300, 300, 250, 100);
            setVisible(true);

            signInBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if(signIn(socket)){
                        return;
                    }
                }
            });

            signUpBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if(signUp(socket)){
                        return;
                    }
                }
            });

        }catch(IOException e){
            e.printStackTrace();
        }

    }

    public boolean signIn(final Socket socket){
        final JTextField idText;
        final JTextField passwordText;
        JButton goBtn;

        try {
            final DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF("in");

            setSize(400, 600);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            setLayout(new FlowLayout());
            JLayeredPane pane = new JDesktopPane();
            pane.setBounds(0, 0, 400, 800);
            pane.setLayout(null);

            // set id text field
            idText = new JTextField(20);
            idText.setBounds(200, 200, 80, 50);
            pane.add(idText);
            idText.setOpaque(false);
            idText.setForeground(Color.LIGHT_GRAY);
            idText.setBorder(javax.swing.BorderFactory.createEmptyBorder());

            // set password text field
            passwordText = new JTextField(20);
            passwordText.setBounds(200, 300, 80, 50);
            passwordText.setOpaque(false);
            passwordText.setForeground(Color.LIGHT_GRAY);
            passwordText.setBorder(javax.swing.BorderFactory.createEmptyBorder());
            pane.add(passwordText);

            // set confirm button
            goBtn = new JButton();
            goBtn.setBounds(200, 400, 30, 100);
            pane.add(goBtn);

            final DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

            goBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        id = idText.getText();
                        password = getSHA256(passwordText.getText());
                        dataOutputStream.writeUTF(id);
                        dataOutputStream.writeUTF(password);

                        String result = dataInputStream.readUTF();
                        if (result.equals("success")) {
                            name = dataInputStream.readUTF();
                        }else{
                            idText.setText("");
                            passwordText.setText("");
                        }
                    }catch(Exception ee){
                        ee.printStackTrace();
                    }
                    return;
                }
        });

            setVisible(true);

        }catch(IOException e){
            e.printStackTrace();
        }catch(Exception ee){
            ee.printStackTrace();
        }
        return false;
    }

    public boolean signUp(Socket socket){
        final JTextField nameText;
        final JTextField idText;
        final JTextField passwordText;
        JButton goBtn;

        try{
            final DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

            setSize(400, 600);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            setLayout(new FlowLayout());
            JLayeredPane pane = new JDesktopPane();
            pane.setBounds(0, 0, 400, 600);
            pane.setLayout(null);

            // set name text field
            nameText = new JTextField(20);
            nameText.setBounds(200, 100, 80, 50);
            pane.add(nameText);
            nameText.setOpaque(false);
            nameText.setForeground(Color.LIGHT_GRAY);
            nameText.setBorder(javax.swing.BorderFactory.createEmptyBorder());

            // set id text field
            idText = new JTextField(20);
            idText.setBounds(200, 200, 80, 50);
            pane.add(idText);
            idText.setOpaque(false);
            idText.setForeground(Color.LIGHT_GRAY);
            idText.setBorder(javax.swing.BorderFactory.createEmptyBorder());

            // set password text field
            passwordText = new JTextField(20);
            passwordText.setBounds(200, 300, 80, 50);
            passwordText.setOpaque(false);
            passwordText.setForeground(Color.LIGHT_GRAY);
            passwordText.setBorder(javax.swing.BorderFactory.createEmptyBorder());
            pane.add(passwordText);

            // set confirm button
            goBtn = new JButton();
            goBtn.setBounds(400, 550, 30, 100);
            pane.add(goBtn);

            final DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

            goBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        name = nameText.getText();
                        id = idText.getText();
                        password = getSHA256(passwordText.getText());
                        dataOutputStream.writeUTF(name);
                        dataOutputStream.writeUTF(id);
                        dataOutputStream.writeUTF(password);

                        String result = dataInputStream.readUTF();
                        if (result.equals("success")) {
                            name = dataInputStream.readUTF();
                        }else{
                            idText.setText("");
                            passwordText.setText("");
                        }
                    }catch(Exception ee){
                        ee.printStackTrace();
                    }
                    return;
                }
            });
            setVisible(true);
        }catch(IOException e){
            e.printStackTrace();
        }catch(Exception ee){
            ee.printStackTrace();
        }
        return false;
    }

    static public String getId(){
        return id;
    }

    static public String getMyName() {
        return name;
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

public class myClientFrame extends JFrame {
    JTextArea messageArea = new JTextArea();
    JTextField messageField = new JTextField(100);
    JButton sendBtn = new JButton("send");
    JButton exitBtn = new JButton("exit");
    JPanel panel = new JPanel();
    Socket socket;
    String name = "";
    ClientSender sender;

    public myClientFrame(Socket socket){
        super("Chat Room");
        // sign in / sign up
        WelcomeScreen welcomeScreen = new WelcomeScreen(socket);

        // enter chat room
        this.socket = socket;

        add("Center", messageField);
        panel.add(messageField);
        panel.add(sendBtn);
        panel.add(exitBtn);
        add("South", panel);

        sendBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sender.start();
            }
        });

        exitBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(300, 300, 350, 300);
        setVisible(false);
    }
}

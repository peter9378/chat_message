package myClient;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;

import myClient.myClient.*;

public class myClientFrame extends JFrame implements ActionListener {
    JTextArea messageArea = new JTextArea();
    JTextField messageField = new JTextField(15);
    JButton sendBtn = new JButton("send");
    JButton exitBtn = new JButton("exit");
    JPanel panel = new JPanel();
    Socket socket;
    ClientSender sender;

    public myClientFrame(Socket socket, String name){
        super("Chat Room");
        this.socket = socket;
        sender = new ClientSender(this, name);


        add("Center", messageArea);
        panel.add(messageField);
        panel.add(sendBtn);
        panel.add(exitBtn);
        add("South", panel);

        sendBtn.addActionListener(this);

        exitBtn.addActionListener(this);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(300, 300, 350, 300);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e){
        // send button click
        if(e.getSource() == sendBtn){
            if(messageField.getText().equals("")){
                return;
            }
            // send message to server
            sender.sendMessage();
            messageField.setText("");
        }else{
            this.dispose();
        }
    }
}

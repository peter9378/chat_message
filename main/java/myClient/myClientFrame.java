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
    JScrollPane scroll = new JScrollPane(messageArea);
    JPanel panel = new JPanel();
    Socket socket;
    ClientSender sender;
    JLabel statusLabel = new JLabel();


    public myClientFrame(Socket socket, String name){
        super("Chat Room");
        this.socket = socket;
        sender = new ClientSender(this, name);

        add("North", statusLabel);
        messageArea.setEnabled(false);
        statusLabel.setAlignmentX(JLabel.CENTER);
        add("Center", scroll);
        panel.add(messageField);
        panel.add(sendBtn);
        panel.add(exitBtn);
        add("South", panel);

        messageField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendBtn.doClick();
            }
        });

        sendBtn.addActionListener(this);
        exitBtn.addActionListener(this);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(300, 300, 400, 350);
        setVisible(true);
        setResizable(false);
    }

    public void actionPerformed(ActionEvent e){
        // send button click
        if(e.getSource() == sendBtn){
            String text = messageField.getText();

            // hot command setting
            if(text.equals("")){
                return;
            }else if(text.equals("/online")){
                this.statusLabel.setText("online");
            }else if(text.equals("/offline")){
                this.statusLabel.setText("offline");
            }else if(text.equals("/busy")){
                this.statusLabel.setText("busy");
            }

            // send message to server
            sender.sendMessage();
            messageField.setText("");
        }else{
            this.dispose();
        }
    }
}

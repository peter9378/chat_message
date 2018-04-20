package myClient;

import user.UserDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SigninFrame extends JFrame implements ActionListener {
    private JTextField idField;
    private JPasswordField passwordField;
    private JButton signinBtn;
    private myClient client;
    private UserDAO userDAO;

    public static void main(String[] args) throws Exception {
        SigninFrame frame = new SigninFrame();
        UserDAO userDAO = new UserDAO();
        userDAO.getConnection();
        frame.setDAO(userDAO);
    }

    public SigninFrame(){
        super("Sign in");
        setSize(500, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(null);
        JLayeredPane pane = new JLayeredPane();
        pane.setBounds(0, 0, 500, 800);
        pane.setLayout(null);

        // set id text field
        idField = new JTextField(20);
        idField.setBounds(200, 200, 80, 50);
        pane.add(idField);
        idField.setOpaque(false);
        idField.setForeground(Color.LIGHT_GRAY);
        idField.setBorder(javax.swing.BorderFactory.createEmptyBorder());


        // set password text field
        passwordField = new JPasswordField(20);
        passwordField.setBounds(200, 300, 80, 50);
        passwordField.setOpaque(false);
        passwordField.setForeground(Color.LIGHT_GRAY);
        passwordField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        pane.add(passwordField);


        // set confirm button
        signinBtn = new JButton();
        signinBtn.setBounds(200, 400, 30, 100);
        signinBtn.addActionListener(this);

        pane.add(signinBtn);
        add(pane);
        setVisible(true);
    }

    public void setClient(myClient client){
        this.client = client;
    }

    public void setDAO(UserDAO userDAO){
        this.userDAO = userDAO;
    }

    @Override
    public void actionPerformed(ActionEvent e){
        try {
            if (idField.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "please enter id", "error message", JOptionPane.INFORMATION_MESSAGE);
            } else if (new String(passwordField.getPassword()).equals("")) {
                JOptionPane.showMessageDialog(null, "please enter password", "error message", JOptionPane.INFORMATION_MESSAGE);
            } else {
                boolean signinCheck = userDAO.isCorrectUser(idField.getText(), userDAO.getSHA256(new String(passwordField.getPassword())));
                if (signinCheck) {
                    JOptionPane.showMessageDialog(null, "welcome, " + userDAO.getNameById(idField.getText()), "sign in success", JOptionPane.INFORMATION_MESSAGE);
                    myClient.showChatFrame(this);


                }
            }
        }catch(Exception ee){
            ee.printStackTrace();
        }
    }



}

package myClient;

import user.UserDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignupFrame extends JFrame implements ActionListener {
    private JTextField nameField;
    private JTextField idField;
    private JPasswordField passwordField;
    private JButton signupBtn;
    private myClient client;
    private UserDAO userDAO;

    public static void main(String[] args) throws Exception{
        SignupFrame frame = new SignupFrame();
        UserDAO userDAO = new UserDAO();
        userDAO.getConnection();
        frame.setDAO(userDAO);
    }

    public SignupFrame(){
        super("Sign up");
        setSize(500, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(null);
        JLayeredPane pane = new JLayeredPane();
        pane.setBounds(0, 0, 500, 800);
        pane.setLayout(null);

        // set name text field
        nameField = new JTextField(20);
        nameField.setBounds(200, 200, 80, 50);
        pane.add(nameField);
        nameField.setOpaque(false);
        nameField.setForeground(Color.LIGHT_GRAY);
        nameField.setBorder(javax.swing.BorderFactory.createEmptyBorder());

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
        signupBtn = new JButton();
        signupBtn.setBounds(200, 400, 30, 100);
        signupBtn.addActionListener(this);

        pane.add(signupBtn);
        add(pane);
        setVisible(false);
    }

    public void setClient(myClient client){
        this.client = client;
    }

    public void setDAO(UserDAO userDAO){
        this.userDAO = userDAO;
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(nameField.getText().equals("")){
            JOptionPane.showMessageDialog(null, "please enter name", "error message", JOptionPane.INFORMATION_MESSAGE);
        }else if(idField.getText().equals("")){
            JOptionPane.showMessageDialog(null, "please enter id", "error message", JOptionPane.INFORMATION_MESSAGE);
        }else if(new String(passwordField.getPassword()).equals("")){
            JOptionPane.showMessageDialog(null, "please enter password", "error message", JOptionPane.INFORMATION_MESSAGE);
        }else{
            try {
                boolean signupCheck = userDAO.isCorrectUser(idField.getText(), userDAO.getSHA256(new String(passwordField.getPassword())));
                if (signupCheck) {
                    JOptionPane.showMessageDialog(null, "welcome, " + userDAO.getNameById(idField.getText()), "sign in success", JOptionPane.INFORMATION_MESSAGE);
                    myClient.showChatFrame(this);
                }
            }catch(Exception ee){
                ee.printStackTrace();
            }
        }
    }
}

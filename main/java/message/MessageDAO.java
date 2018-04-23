package message;

import myDB.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class MessageDAO {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    public MessageDAO(){ }

    // initialize constructor
    public Connection getConnection() throws SQLException{
        DBUtil dbUtil = new DBUtil();
        Connection con;
        con = dbUtil.getConnection();
        return con;
    }

    // add message to database
    public void addMessage(Message message){
        try {
            String query = "INSERT into MessageBox VALUES(?, ?, ?)";
            connection = getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, message.index);
            preparedStatement.setString(2, message.text);
            preparedStatement.setTimestamp(3, message.timestamp);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            try{
                if(preparedStatement!=null){
                    preparedStatement.close();
                }
                if(connection!=null){
                    connection.close();
                }
            }catch(SQLException e){
                e.printStackTrace();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    // get unread message by index
    public LinkedList<Message> getUnreadMessage(int index){
        LinkedList<Message> unreadMessageList = new LinkedList<Message>();
        try {
            String queryString = "SELECT * FROM MessageBox where MessageBox.index > " + Integer.toString(index-1) + ";";
            connection = getConnection();
            preparedStatement = connection.prepareStatement(queryString);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                unreadMessageList.add(new Message(resultSet.getInt("index"), resultSet.getString("message"), resultSet.getTimestamp("time")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
                if (preparedStatement != null)
                    preparedStatement.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return unreadMessageList;
    }

    // get number of messages
    public int getSize(){
        try {
            String queryString = "SELECT COUNT(*) FROM MessageBox;";
            connection = getConnection();
            preparedStatement = connection.prepareStatement(queryString);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
                if (preparedStatement != null)
                    preparedStatement.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    // get all messages
    public LinkedList<Message> getAllMessages(){
        LinkedList<Message> messageList = new LinkedList<Message>();
        try {
            String queryString = "SELECT * FROM MessageBox;";
            connection = getConnection();
            preparedStatement = connection.prepareStatement(queryString);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                messageList.add(new Message(resultSet.getInt("index"), resultSet.getString("message"), resultSet.getTimestamp("time")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
                if (preparedStatement != null)
                    preparedStatement.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return messageList;
    }
}

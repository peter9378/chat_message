import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageDAO {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    public MessageDAO(){

    }

    public Connection getConnection() throws SQLException{
        DBUtil dbUtil = new DBUtil();
        Connection con;
        con = dbUtil.getConnection();
        return con;
    }

    public void addMessage(Message message){
        try {
            String query = "INSERT into MessageBox(index, message, time) VALUES(?, ?, ?)";
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
    public void getUnreadMessage(int index){
    }
}

package user;

import myDB.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class UserDAO {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    public UserDAO() {

    }

    public Connection getConnection() throws SQLException {
        DBUtil dbUtil = new DBUtil();
        Connection con;
        con = dbUtil.getConnection();
        return con;
    }

    public void addUser(User user) {
        try {
            String query = "INSERT into User(id, name, password, index) VALUES(?, ?, ?, ?)";

            connection = getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, user.getId());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setInt(4, user.getIndex());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteUser(String id) {
        try {
            String queryString = "DELETE FROM User WHERE id=?";
            connection = getConnection();
            preparedStatement = connection.prepareStatement(queryString);
            preparedStatement.setString(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
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
    }

    public LinkedList<User> getUserList() {
        LinkedList<User> userList = new LinkedList<User>();
        try {
            String queryString = "SELECT * FROM User";
            connection = getConnection();
            preparedStatement = connection.prepareStatement(queryString);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                userList.add(new User(resultSet.getString("id"), resultSet.getString("name"), resultSet.getInt("index")));
                // print line
                System.out.println("id : " + resultSet.getString("id") + "\nname : " + resultSet.getString("name"));
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
        return userList;
    }

    public boolean isIdExist(String id){
        try {
            String queryString = "SELECT User.id FROM User WHERE id=?";
            connection = getConnection();
            preparedStatement = connection.prepareStatement(queryString);
            preparedStatement.setString(1, id);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.wasNull()){
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
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
        return true;
    }

    public boolean isCorrectUser(String id, String password){
        try{
            String queryString = "SELECT User.password FROM User WHERE id=?";
            connection = getConnection();
            preparedStatement = connection.prepareStatement(queryString);
            preparedStatement.setString(1, id);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            if(resultSet.getString("password").equals(password)){
                return true;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try {
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
        return false;
    }
}

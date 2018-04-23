package user;

import myDB.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;


public class UserDAO {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    DBUtil db = new DBUtil();

    public UserDAO() { }

    // add user to database
    public void addUser(User user) {
        try {
            String query = "insert into User values(?, ?, ?, ?, ?);";
            connection = db.getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, user.getId());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getStatus());
            preparedStatement.setInt(5, user.getIndex());
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

    // get user list from database
    public LinkedList<User> getUserList() {
        LinkedList<User> userList = new LinkedList<User>();
        try {
            String queryString = "SELECT * FROM User";
            connection = db.getConnection();
            preparedStatement = connection.prepareStatement(queryString);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                userList.add(new User(resultSet.getString("id"), resultSet.getString("name"),
                        "", resultSet.getString("status"), resultSet.getInt("index")));
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

    // check id overlap
    public boolean isIdExist(String id){
        try {
            String queryString = "SELECT * FROM User WHERE id=?";
            connection = db.getConnection();
            preparedStatement = connection.prepareStatement(queryString);
            preparedStatement.setString(1, id);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            if(resultSet.getString("id").equals(id)){
                return true;
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
        return false;
    }

    // authentication
    public boolean isCorrectUser(String id, String password){
        try{
            String queryString = "SELECT * FROM User WHERE id=?";
            connection = db.getConnection();
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

    // get name by user id
    public String getNameById(String id){
        String name = "";
        try{
            String queryString = "SELECT * FROM User WHERE id=?";
            connection = db.getConnection();
            preparedStatement = connection.prepareStatement(queryString);
            preparedStatement.setString(1, id);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            name = resultSet.getString("name");
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
        return name;
    }

    // get current index by user id
    public int getIndexById(String id){
        int index = -1;
        try{
            String queryString = "SELECT * FROM User WHERE id=?";
            connection = db.getConnection();
            preparedStatement = connection.prepareStatement(queryString);
            preparedStatement.setString(1, id);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            index = resultSet.getInt("index");
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
        return index;
    }

    // update user status by id
    public void updateUserStatus(String id, String status){
        try{
            String queryString = "UPDATE User set status=? WHERE id=?";
            connection = db.getConnection();
            preparedStatement = connection.prepareStatement(queryString);
            preparedStatement.setString(1, status);
            preparedStatement.setString(2, id);
            preparedStatement.executeUpdate();
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
    }

    // set user current index by id
    public void setUserIndexById(int index, String id){
        try{
            String queryString = "UPDATE User set User.index=? WHERE id=?";
            connection = db.getConnection();
            preparedStatement = connection.prepareStatement(queryString);
            preparedStatement.setInt(1, index);
            preparedStatement.setString(2, id);
            preparedStatement.executeUpdate();
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
    }

    // set user current index by name
    public void setUserIndexByName(int index, String name){
        try{
            String queryString = "UPDATE User set User.index=? WHERE name=?";
            connection = db.getConnection();
            preparedStatement = connection.prepareStatement(queryString);
            preparedStatement.setInt(1, index);
            preparedStatement.setString(2, name);
            preparedStatement.executeUpdate();
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
    }

    // get user status by name
    public String getStatusByName(String name){
        String status = "";
        try{
            String queryString = "SELECT * FROM User WHERE name=?";
            connection = db.getConnection();
            preparedStatement = connection.prepareStatement(queryString);
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            status = resultSet.getString("status");
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
        return status;
    }
}

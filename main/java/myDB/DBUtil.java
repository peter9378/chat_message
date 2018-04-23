package myDB;

import java.sql.*;

public class DBUtil {
    private String url = "jdbc:mysql://210.89.188.165:3306/Chat";   // database(MySQL) address
    private String id = "peter9378";    // id
    private String password = "peter";  // password
    private String driver = "com.mysql.jdbc.Driver";    // driver

    static Statement statement = null;

    // default constructor
    public DBUtil(){
        try {
            // load JDBC driver
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // connect to database and return it
    public Connection getConnection()throws SQLException{
        Connection con= null;
        con = DriverManager.getConnection(url, id, password);

        return con;
    }
}

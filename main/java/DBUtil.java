import java.sql.*;

public class DBUtil {
    private String url = "jdbc:mysql://210.89.188.165:3306";
    private String user = "peter9378";
    private String password = "peter";
    private String driver = "com.mysql.jdbc.Driver";

    static Connection con = null;
    static Statement statement = null;
    static ResultSet resultSet = null;


    public DBUtil(){
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public DBUtil(String url, String user, String password, String driver) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.driver = driver;
        new DBUtil();
    }

    public Connection getConnection()throws SQLException{

        Connection con= null;
        con = DriverManager.getConnection(url, user, password);

        return con;
    }

    public void insert(String query){
        try{
            statement.executeUpdate(query);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void delete(){

    }

}

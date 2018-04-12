import java.sql.*;
import java.util.Scanner;

public class DBUtil {
    Connection con = null;
    Statement statement = null;
    ResultSet resultSet = null;
    int result = 0;

    try{
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
        try {
            con = DriverManager.getConnection("jdbc:mysql://http://210.89.188.165:3306" +
            "characterEncoding=utf-8", "doolda", "ehdwls");
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);
    }
}

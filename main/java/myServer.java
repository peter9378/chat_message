import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class myServer {
    public static void main(String args[]) throws IOException {
        int number, temp;
        ServerSocket s1 = new ServerSocket(1234);
        Socket ss = s1.accept();
        Scanner sc = new Scanner(ss.getInputStream());
        number = sc.nextInt();

        temp = number*5;

        PrintStream ps = new PrintStream(ss.getOutputStream());
        ps.println(temp);

    }
}

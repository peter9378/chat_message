import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class myClient {
    public static void main(String args[]) throws IOException {
        int number, temp;
        Scanner sc = new Scanner(System.in);
        Socket socket = new Socket("127.0.0.1", 1234);
        Scanner scanner1 = new Scanner(socket.getInputStream());
        System.out.println("Enter any number");
        number = sc.nextInt();

        PrintStream p = new PrintStream(socket.getOutputStream());
        p.println(number);
        temp = scanner1.nextInt();
        System.out.println(temp);
    }
}

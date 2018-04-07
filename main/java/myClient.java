import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.Socket;


public class myClient {
    public static void main(String args[]) throws IOException {
        try {
            String testIp = "127.0.0.1"; // test local ip

            // create new socket and send request to server
            Socket socket = new Socket(testIp, 1234);

            // get input stream of socket
            InputStream inputStream = socket.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(inputStream);

            // print data from socket
            System.out.println("message from server : " + dataInputStream.readUTF());

            // close stream and socket
            dataInputStream.close();
            socket.close();
            System.out.println("connection has closed");
        }catch(ConnectException ce) {
            ce.printStackTrace();
        }catch(IOException ie){
            ie.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

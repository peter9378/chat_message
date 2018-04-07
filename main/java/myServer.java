import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class myServer {
    public static void main(String args[]) throws IOException {
        ServerSocket serverSocket = null;
        try{
            serverSocket = new ServerSocket(1234);
            System.out.println(getTime() + "server ready");
        }catch(IOException e){
            e.printStackTrace();
        }

        while(true){
            try{
                System.out.println(getTime() + "wait request from client");

                // create new socket
                Socket socket = serverSocket.accept();
                System.out.println(getTime() + "request from " + socket.getInetAddress());

                // get output stream of socket
                OutputStream outputStream = socket.getOutputStream();
                DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

                // send data to remote socket
                dataOutputStream.writeUTF("test message from server");
                System.out.println(getTime() + "data send complete");

                // close stream and socket
                dataOutputStream.close();
                socket.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    static String getTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("[hh:mm:ss]");
        return sdf.format(new Date());
    }
}

//Seth Sutton
import java.io.*;
import java.net.*;


public class Dispatcher {
    public static ServerSocket port;
    public static void main(String[] args){
        try {
            port = new ServerSocket(9877);
            System.out.println("Server started on port " + port.getLocalPort());
            while(true){
                Socket connectToClient = port.accept();
                System.out.println("Client connection accepted: " + connectToClient.getInetAddress().getHostAddress());
                ServerThread serverThread = new ServerThread(connectToClient);
                serverThread.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


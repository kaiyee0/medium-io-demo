import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import java.lang.Thread;

public class BioServer {
  public static void main(String[] args) {
    try {
      ServerSocket srvSocket = new ServerSocket(8888);
      System.out.println("socket start");
      Socket socket = srvSocket.accept();
      while(socket.isConnected()) {
        System.out.println("get client");
        InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String clientMessage = bufferedReader.readLine();
        System.out.println("message from client: " + clientMessage);

        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
        BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
        bufferedWriter.write("hello this is server \n");
        bufferedWriter.flush();
        socket = srvSocket.accept();
      }
    } catch (IOException e){
      e.printStackTrace();
    }
  }
}
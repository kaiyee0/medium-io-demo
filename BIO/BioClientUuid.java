import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.UUID;

public class BioClientUuid {
  public static void main(String[] args) {
    try{
      Socket socket = new Socket("127.0.0.1", 8888);
      OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
      BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
      String uuid = UUID.randomUUID().toString();
      bufferedWriter.write("hello this is client " + uuid + "\n");
      bufferedWriter.flush();

      InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
      BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
      String serverMessage = bufferedReader.readLine();
      System.out.println("message from server: " + serverMessage);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
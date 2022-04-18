import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import java.lang.Thread;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ArrayBlockingQueue;

public class BioServerThread {
  public static void main(String[] args) {
    try {
      ServerSocket srvSocket = new ServerSocket(8888);
      System.out.println("socket start");
      ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 100, 1000, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(50));

      while(true) {
        Socket socket = srvSocket.accept();
        if (socket.isConnected()) {
          executor.execute(() -> {
            System.out.println("thread created");
            try {
              final Socket tSocket = socket;
              InputStreamReader inputStreamReader = new InputStreamReader(tSocket.getInputStream());
              BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
              String clientMessage = bufferedReader.readLine();
              System.out.println("message from client: " + clientMessage);

              // implement sleep to wait
              try {
                Thread.sleep(2000);
              } catch (InterruptedException e) {
                return;
              }

              OutputStreamWriter outputStreamWriter = new OutputStreamWriter(tSocket.getOutputStream());
              BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
              bufferedWriter.write("hello this is server \n");
              bufferedWriter.flush();
              System.out.println("end of request");
            }
            catch (IOException e) {
              e.printStackTrace();
            }
          });
        }
      }
    } catch (IOException e){
      e.printStackTrace();
    }
  }
}
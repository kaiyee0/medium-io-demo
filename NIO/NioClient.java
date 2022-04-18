import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import java.lang.Thread;
import java.util.Iterator;
import java.util.UUID;

public class NioClient {
  public static void main(String[] args) throws IOException {
    ByteBuffer buffer = ByteBuffer.allocate(1024);
    ByteBuffer readerBuffer = ByteBuffer.allocate(1024);
    try {
      SocketChannel socketChannel = SocketChannel.open();
      socketChannel.configureBlocking(true);
      socketChannel.connect(new InetSocketAddress(8888));
      String uuid = UUID.randomUUID().toString();
      buffer.put(new String("hello this is client " + uuid + "\n").getBytes());
      buffer.flip();
      socketChannel.write(buffer);
      buffer.clear();
      
      socketChannel.read(readerBuffer);
      System.out.println("message from server: " + new String(readerBuffer.array()));
      readerBuffer.clear();
      socketChannel.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import java.lang.Thread;

import java.util.Iterator;

public class NioServer {
  public static void main(String[] args) throws IOException {
    Selector selector = Selector.open();
    ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
    serverSocketChannel.socket().bind(new InetSocketAddress(8888));
    serverSocketChannel.configureBlocking(false);
    ByteBuffer buffer = ByteBuffer.allocate(1024);
    serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

    System.out.println("nio start");
    while (true) {
      try {
        int n = selector.select(2000);
        if (n == 0) {
          continue;
        }
        Iterator<SelectionKey> selectionKeyIter = selector.selectedKeys().iterator();
        while (selectionKeyIter.hasNext()) {
          SelectionKey selectionKey = selectionKeyIter.next();
          // check which event is able to handle
          if (selectionKey.isAcceptable()) {
            ServerSocketChannel sSocketChannel = (ServerSocketChannel) selectionKey.channel();
            SocketChannel socketChannel = sSocketChannel.accept();
            socketChannel.configureBlocking(false);  
            socketChannel.register(selector, SelectionKey.OP_READ);
            System.out.println("get client message");
          }
          else if (selectionKey.isReadable()) {
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            int count;
            buffer.clear();
            while ((count = socketChannel.read(buffer)) > 0) {
              buffer.flip();
              byte[] bytes = new byte[buffer.remaining()];
              buffer.get(bytes);
              System.out.println("message from client: " + new String(bytes));
            }
            socketChannel.write(ByteBuffer.wrap(new String("hello this is server \n").getBytes()));
            socketChannel.close();
          } 
          selectionKeyIter.remove();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import java.lang.Thread;

import java.util.Iterator;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ArrayBlockingQueue;

public class NioServerThread {
  public static void main(String[] args) throws IOException {
    Selector selector = Selector.open();
    ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
    serverSocketChannel.socket().bind(new InetSocketAddress(8888));
    serverSocketChannel.configureBlocking(false);
    
    serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 100, 1000, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(50));

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
            selectionKey.interestOps(0);
            System.out.println("read client message");
            executor.execute(() -> {
              try {
                SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                int count;
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                buffer.clear();
                // implement sleep to wait
                try {
                  Thread.sleep(2000);
                } catch (InterruptedException e) {
                  return;
                }
                while ((count = socketChannel.read(buffer)) > 0) {
                  buffer.flip();
                  byte[] bytes = new byte[buffer.remaining()];
                  buffer.get(bytes);
                  System.out.println("message from client: " + new String(bytes));
                }
                socketChannel.write(ByteBuffer.wrap(new String("hello this is server \n").getBytes()));
                socketChannel.close();
              } catch (IOException e) {
                e.printStackTrace();
              }
            });
          } 
          selectionKeyIter.remove();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
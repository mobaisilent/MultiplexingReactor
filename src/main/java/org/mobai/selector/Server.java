import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

// 这里有一个隐式声明，需要将最上面的package移除

public static void main(String[] args) {
  try (ServerSocketChannel serverChannel = ServerSocketChannel.open();
       Selector selector = Selector.open()) {
    // 创建ServerSocket和Selector
    serverChannel.bind(new InetSocketAddress(8080));
    serverChannel.configureBlocking(false);
    serverChannel.register(selector, SelectionKey.OP_ACCEPT);
    while (true) {
      int count = selector.select();
      System.out.println("监听到 " + count + " 个事件");
      Set<SelectionKey> selectionKeys = selector.selectedKeys();
      Iterator<SelectionKey> iterator = selectionKeys.iterator();
      while (iterator.hasNext()) {
        SelectionKey key = iterator.next();
        if (key.isAcceptable()) {
          System.out.println("here1");
          SocketChannel channel = serverChannel.accept();
          System.out.println("客户端已连接，IP地址为：" + channel.getRemoteAddress());
          channel.configureBlocking(false);
          channel.register(selector, SelectionKey.OP_READ);
        } else if (key.isReadable()) {
          System.out.println("here2");
          SocketChannel channel = (SocketChannel) key.channel();
          ByteBuffer buffer = ByteBuffer.allocate(128);
          channel.read(buffer);
          buffer.flip();
          System.out.println("接收到客户端数据：" + new String(buffer.array(), 0, buffer.remaining()));
          channel.write(ByteBuffer.wrap("已收到！".getBytes()));
        }
        iterator.remove();
      }
    }
  } catch (IOException e) {
    throw new RuntimeException(e);
  }
}
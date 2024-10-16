import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public static void main(String[] args) {
  try (SocketChannel channel = SocketChannel.open(new InetSocketAddress("localhost", 8080));
       Scanner scanner = new Scanner(System.in)){
    System.out.println("已连接到服务端！");
    while (true) {
      System.out.println("请输入要发送给服务端的内容：");
      String text = scanner.nextLine();
      channel.write(ByteBuffer.wrap(text.getBytes()));
      System.out.println("已发送！");
      ByteBuffer buffer = ByteBuffer.allocate(128);
      channel.read(buffer);
      buffer.flip();
      System.out.println("收到服务器返回："+new String(buffer.array(), 0, buffer.remaining()));
    }
  } catch (IOException e) {
    throw new RuntimeException(e);
  }
}
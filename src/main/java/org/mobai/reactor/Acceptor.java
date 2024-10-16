package org.mobai.reactor;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Acceptor主要用于处理连接操作
 */
public class Acceptor implements Runnable{

  private final ServerSocketChannel serverChannel;
  private final Selector selector;

  public Acceptor(ServerSocketChannel serverChannel, Selector selector) {
    this.serverChannel = serverChannel;
    this.selector = selector;
  }

  @Override
  public void run() {
    try{
      SocketChannel channel = serverChannel.accept();
      System.out.println("客户端已连接，IP地址为："+channel.getRemoteAddress());
      channel.configureBlocking(false);
      channel.register(selector, SelectionKey.OP_READ, new Handler(channel));
    }catch (IOException e){
      e.printStackTrace();
    }
  }
}
package org.mobai.reactor;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Reactor implements Closeable, Runnable{

  private final ServerSocketChannel serverChannel;
  private final Selector selector;
  public Reactor() throws IOException {
    serverChannel = ServerSocketChannel.open();
    selector = Selector.open();
  }

  @Override
  public void run() {
    try {
      serverChannel.bind(new InetSocketAddress(8080));
      serverChannel.configureBlocking(false);
      serverChannel.register(selector, SelectionKey.OP_ACCEPT, new Acceptor(serverChannel, selector));
      while (true) {
        int count = selector.select();
        System.out.println("监听到 "+count+" 个事件");
        Set<SelectionKey> selectionKeys = selector.selectedKeys();
        Iterator<SelectionKey> iterator = selectionKeys.iterator();
        while (iterator.hasNext()) {
          this.dispatch(iterator.next());
          iterator.remove();
        }
      }
    }catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void dispatch(SelectionKey key){
    Object att = key.attachment();
    if(att instanceof Runnable) {
      ((Runnable) att).run();
    }
  }

  @Override
  public void close() throws IOException {
    serverChannel.close();
    selector.close();
  }
}
import org.mobai.reactor.Reactor;

import java.io.IOException;

public static void main(String[] args) {
  try (Reactor reactor = new Reactor()){
    reactor.run();
  }catch (IOException e) {
    e.printStackTrace();
  }
}
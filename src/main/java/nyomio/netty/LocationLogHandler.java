package nyomio.netty;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import nyomio.data.TrafficLog;
import org.springframework.stereotype.Component;

@Component
public class LocationLogHandler {

  BlockingQueue<TrafficLog> queue = new LinkedBlockingQueue<>(1000);

  public void onLocationarrived(TrafficLog location) {
    try {
      queue.put(location);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public TrafficLog getLocation() throws InterruptedException {
    return queue.take();
  }

}

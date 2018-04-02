package flink.example.netty;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.stereotype.Component;

import flink.example.Location;

@Component
public class LocationLogHandler {

	BlockingQueue<Location> queue = new LinkedBlockingQueue<>(1000);

	public void onLocationarrived(Location location) {
		try {
			queue.put(location);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public Location getLocation() throws InterruptedException {
		return queue.take();
	}

}

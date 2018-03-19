package flink.example;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class LocationSource {

	private static final int LOCATION_NUMBER = 10;
	private static final int TIME_STEP = 1000 * 60;

	public List<Location> getLocations() {
		List<Location> locations = new ArrayList<>();
		Long time = System.currentTimeMillis() - (LOCATION_NUMBER * TIME_STEP);
		for (int i = 0; i < LOCATION_NUMBER; i++) {
			time += TIME_STEP;
			locations.add(new Location(time, "127.0.0.1", "message"));
		}
		return locations;
	}

}

package flink.example;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class LocationSource {

	private static final int LOCATION_NUMBER = 10;
	private static final int TIME_STEP = 10;
	private static final double COORDINATE_STEP = 0.01;

	public List<Location> getLocations() {
		List<Location> locations = new ArrayList<>();
		Long time = System.currentTimeMillis() - (LOCATION_NUMBER * TIME_STEP);
		Double lat = 47.49;
		Double lng = 19.04;
		for (int i = 0; i < LOCATION_NUMBER; i++) {
			time += TIME_STEP;
			lat += COORDINATE_STEP;
			lng += COORDINATE_STEP;
			locations.add(new Location(time, lat, lng));
		}
		return locations;
	}

}

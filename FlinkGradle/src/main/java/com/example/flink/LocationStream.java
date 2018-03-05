package com.example.flink;

import java.util.HashMap;
import java.util.Map;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.springframework.stereotype.Component;

import com.metamx.tranquility.flink.BeamSink;

@Component
public class LocationStream {

	private LocationSource locationSource;
	private MyBeamFactory beamFactory;

	public LocationStream(LocationSource locationSource, MyBeamFactory beamFactory) {
		this.locationSource = locationSource;
		this.beamFactory = beamFactory;
	}

	public void precessStream() {
		// get the execution environment
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		// get input data by connecting to the socket
		DataStream<Location> locationStream = env.fromCollection(locationSource.getLocations());

		DataStream<Map<String, Object>> mapStream = locationStream
				.map(new MapFunction<Location, Map<String, Object>>() {
					public Map<String, Object> map(Location location) throws Exception {
						Map<String, Object> result = new HashMap<>();
						result.put("timestamp", location.getTimestamp());
						result.put("dimValue", location.getDimValue());
						result.put("numValue", location.getNumValue());

						return result;
					}
				});

		mapStream.addSink(new BeamSink(beamFactory, true));

		try {
			env.execute();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

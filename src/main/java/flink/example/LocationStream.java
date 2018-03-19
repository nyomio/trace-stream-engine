package flink.example;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.springframework.stereotype.Component;

import flink.example.cassandra.CassandraLoggerSink;

@Component
public class LocationStream {

	private LocationSource locationSource;
	private CassandraLoggerSink cassandraLoggerSink;

	public LocationStream(LocationSource locationSource, CassandraLoggerSink cassandraLoggerSink) {
		this.locationSource = locationSource;
		this.cassandraLoggerSink = cassandraLoggerSink;
	}

	public void precessStream() {
		// get the execution environment
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		// get input data by connecting to the socket
		DataStream<Location> locationStream = env.fromCollection(locationSource.getLocations());

		locationStream.addSink(cassandraLoggerSink);

		try {
			env.execute();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

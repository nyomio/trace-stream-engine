package flink.example;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.springframework.stereotype.Component;

import flink.example.cassandra.CassandraLoggerSink;
import flink.example.cassandra.CassandraMessageSink;
import flink.example.simpleclient.ParseMessageResult;

@Component
public class LocationStream {

	private LocationSource locationSource;
	private CassandraLoggerSink cassandraLoggerSink;
	private SimpleClientMessageMapFunction simpleClientMessageMapFunction;
	private CassandraMessageSink cassandraMessageSink;

	public LocationStream(LocationSource locationSource, CassandraLoggerSink cassandraLoggerSink,
			SimpleClientMessageMapFunction simpleClientMessageMapFunction, CassandraMessageSink cassandraMessageSink) {
		this.locationSource = locationSource;
		this.cassandraLoggerSink = cassandraLoggerSink;
		this.simpleClientMessageMapFunction = simpleClientMessageMapFunction;
		this.cassandraMessageSink = cassandraMessageSink;
	}

	public void precessStream() {
		// get the execution environment
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		// get input data by connecting to the socket
		DataStream<Location> locationStream = env.addSource(locationSource);
		DataStream<ParseMessageResult> parsedMessageStream = locationStream.map(simpleClientMessageMapFunction);

		locationStream.addSink(cassandraLoggerSink);
		parsedMessageStream.addSink(cassandraMessageSink);

		try {
			env.execute();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

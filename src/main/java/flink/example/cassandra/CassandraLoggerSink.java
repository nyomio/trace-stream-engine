package flink.example.cassandra;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.springframework.stereotype.Component;

import flink.example.Application;
import flink.example.Location;

@Component
public class CassandraLoggerSink extends RichSinkFunction<Location> {

	private CassandraOperations cassandraOperations;

	@Override
	public void open(Configuration parameters) throws Exception {
		cassandraOperations = Application.ctx.getBean(CassandraOperations.class);
	}

	@Override
	public void invoke(Location value) throws Exception {
		cassandraOperations.insert(value);
		// System.out.println(value);
	}
}

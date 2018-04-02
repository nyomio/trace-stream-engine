package flink.example.cassandra;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.springframework.stereotype.Component;

import flink.example.Application;
import flink.example.simpleclient.ParseMessageResult;

@Component
public class CassandraMessageSink extends RichSinkFunction<ParseMessageResult> {

	private CassandraOperations cassandraOperations;

	@Override
	public void open(Configuration parameters) throws Exception {
		cassandraOperations = Application.ctx.getBean(CassandraOperations.class);
	}

	@Override
	public void invoke(ParseMessageResult value) throws Exception {
		cassandraOperations.insertReport(value);
	}
}

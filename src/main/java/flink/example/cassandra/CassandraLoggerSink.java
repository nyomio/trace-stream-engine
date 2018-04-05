package flink.example.cassandra;

import flink.example.Application;
import nyomio.data.TrafficLog;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.springframework.stereotype.Component;

@Component
public class CassandraLoggerSink extends RichSinkFunction<TrafficLog> {

  private CassandraOperations cassandraOperations;

  @Override
  public void open(Configuration parameters) throws Exception {
    cassandraOperations = Application.ctx.getBean(CassandraOperations.class);
  }

  @Override
  public void invoke(TrafficLog value, Context context) throws Exception {
    cassandraOperations.insertLog(value);
  }
}

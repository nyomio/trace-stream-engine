package nyomio.cassandra;

import nyomio.Application;
import nyomio.simpleclient.ParseMessageResult;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.springframework.stereotype.Component;

@Component
public class CassandraMessageSink extends RichSinkFunction<ParseMessageResult> {

  private CassandraOperations cassandraOperations;

  @Override
  public void open(Configuration parameters) throws Exception {
    cassandraOperations = Application.ctx.getBean(CassandraOperations.class);
  }

  @Override
  public void invoke(ParseMessageResult value, Context context) throws Exception {
    cassandraOperations.insertReport(value);
  }
}

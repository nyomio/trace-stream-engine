package nyomio;

import nyomio.cassandra.CassandraLoggerSink;
import nyomio.cassandra.CassandraMessageSink;
import nyomio.simpleclient.ParseMessageResult;
import nyomio.data.TrafficLog;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.springframework.stereotype.Component;

@Component
public class LocationStream {

  private LocationSource locationSource;
  private CassandraLoggerSink cassandraLoggerSink;
  private SimpleClientMessageMapFunction simpleClientMessageMapFunction;
  private CassandraMessageSink cassandraMessageSink;

  public LocationStream(LocationSource locationSource, CassandraLoggerSink cassandraLoggerSink,
      SimpleClientMessageMapFunction simpleClientMessageMapFunction,
      CassandraMessageSink cassandraMessageSink) {
    this.locationSource = locationSource;
    this.cassandraLoggerSink = cassandraLoggerSink;
    this.simpleClientMessageMapFunction = simpleClientMessageMapFunction;
    this.cassandraMessageSink = cassandraMessageSink;
  }

  public void precessStream() {
    // get the execution environment
    final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

    // get input nyomio.data by connecting to the socket
    DataStream<TrafficLog> locationStream = env.addSource(locationSource);
    DataStream<ParseMessageResult> parsedMessageStream = locationStream
        .map(simpleClientMessageMapFunction);

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

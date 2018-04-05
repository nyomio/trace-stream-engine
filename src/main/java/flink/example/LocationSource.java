package flink.example;

import flink.example.netty.LocationLogHandler;
import nyomio.data.TrafficLog;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.springframework.stereotype.Component;

@Component
public class LocationSource extends RichSourceFunction<TrafficLog> {

  private boolean isRunning = true;

  private LocationLogHandler locationHandler;

  @Override
  public void open(Configuration parameters) throws Exception {

    locationHandler = Application.ctx.getBean(LocationLogHandler.class);
  }

  @Override
  public void run(SourceContext<TrafficLog> ctx) throws Exception {
    while (isRunning) {
      TrafficLog location = locationHandler.getLocation();
      ctx.collect(location);
    }
  }

  @Override
  public void cancel() {
    isRunning = false;
  }

}

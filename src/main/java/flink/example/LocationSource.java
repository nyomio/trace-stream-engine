package flink.example;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.springframework.stereotype.Component;

import flink.example.netty.LocationLogHandler;

@Component
public class LocationSource extends RichSourceFunction<Location> {

	private boolean isRunning = true;

	private LocationLogHandler locationHandler;

	@Override
	public void open(Configuration parameters) throws Exception {

		locationHandler = Application.ctx.getBean(LocationLogHandler.class);
	}

	@Override
	public void run(SourceContext<Location> ctx) throws Exception {
		while (isRunning) {
			Location location = locationHandler.getLocation();
			ctx.collect(location);
		}
	}

	@Override
	public void cancel() {
		isRunning = false;
	}

}

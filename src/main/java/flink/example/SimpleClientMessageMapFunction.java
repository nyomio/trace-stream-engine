package flink.example;

import flink.example.simpleclient.ParseMessageResult;
import flink.example.simpleclient.SimpleClientNativeMessageParser;
import nyomio.data.TrafficLog;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.springframework.stereotype.Component;

@Component
public class SimpleClientMessageMapFunction extends
    RichMapFunction<TrafficLog, ParseMessageResult> {

  private SimpleClientNativeMessageParser parser;

  @Override
  public void open(Configuration parameters) throws Exception {
    parser = Application.ctx.getBean(SimpleClientNativeMessageParser.class);
  }

  @Override
  public ParseMessageResult map(TrafficLog value) throws Exception {
    return parser.parseNativeMessage(value);
  }

}

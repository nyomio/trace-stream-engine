package nyomio;

import nyomio.simpleclient.InvalidNativeMessageException;
import nyomio.simpleclient.ParseMessageResult;
import nyomio.simpleclient.SimpleClientNativeMessageParser;
import nyomio.data.TrafficLog;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SimpleClientMessageMapFunction extends
    RichMapFunction<TrafficLog, ParseMessageResult> {

  private static final Logger logger = LoggerFactory.getLogger(SimpleClientMessageMapFunction.class);

  private SimpleClientNativeMessageParser parser;

  @Override
  public void open(Configuration parameters) throws Exception {
    parser = Application.ctx.getBean(SimpleClientNativeMessageParser.class);
  }

  @Override
  public ParseMessageResult map(TrafficLog value) throws Exception {
    try {
      return parser.parseNativeMessage(value);
    } catch (InvalidNativeMessageException e) {
      logger.warn(e.getMessage());
    }
    return new ParseMessageResult();

  }

}

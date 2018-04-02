package flink.example;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.springframework.stereotype.Component;

import flink.example.simpleclient.ParseMessageResult;
import flink.example.simpleclient.SimpleClientNativeMessageParser;

@Component
public class SimpleClientMessageMapFunction extends RichMapFunction<Location, ParseMessageResult> {

	private SimpleClientNativeMessageParser parser;

	@Override
	public void open(Configuration parameters) throws Exception {
		parser = Application.ctx.getBean(SimpleClientNativeMessageParser.class);
	}

	@Override
	public ParseMessageResult map(Location value) throws Exception {
		return parser.parseNativeMessage(value);
	}

}

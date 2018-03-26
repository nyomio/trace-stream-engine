package flink.example.netty;

import java.net.InetSocketAddress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import flink.example.Location;
import flink.example.simpleclient.InvalidNativeMessageException;
import flink.example.simpleclient.ParseMessageResult;
import flink.example.simpleclient.SimpleClientNativeMessageParser;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * Handles a server-side channel.
 */
@Component
@Sharable
public class NettyServerHandler extends ChannelInboundHandlerAdapter { // (1)

	private LocationHandler locationHandler;
	private SimpleClientNativeMessageParser messageParser;

	@Autowired
	public NettyServerHandler(LocationHandler locationHandler,
			SimpleClientNativeMessageParser simpleClientNativeMessageParser) {
		this.locationHandler = locationHandler;
		this.messageParser = simpleClientNativeMessageParser;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
		ByteBuf in = (ByteBuf) msg;
		try {
			ParseMessageResult response = messageParser.parseNativeMessage(in);
			System.out.println(response);
		} catch (InvalidNativeMessageException e) {

			e.printStackTrace();
		}
		try {
			StringBuilder sb = new StringBuilder();
			while (in.isReadable()) { // (1)
				sb.append((char) in.readByte());
			}
			System.out.print(sb.toString());
			System.out.flush();
			locationHandler.onLocationarrived(new Location(System.currentTimeMillis(), sb.toString(),
					((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress()));
		} finally {
			ReferenceCountUtil.release(msg); // (2)
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
		// Close the connection when an exception is raised.
		cause.printStackTrace();
		ctx.close();
	}
}
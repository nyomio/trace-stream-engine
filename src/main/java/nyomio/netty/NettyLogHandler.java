package nyomio.netty;

import nyomio.simpleclient.SimpleClientNativeMessageParser;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import nyomio.data.TrafficLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Handles a server-side channel.
 */
@Component
@Sharable
public class NettyLogHandler extends ChannelInboundHandlerAdapter { // (1)

  private LocationLogHandler locationHandler;

  @Autowired
  public NettyLogHandler(LocationLogHandler locationHandler,
      SimpleClientNativeMessageParser simpleClientNativeMessageParser) {
    this.locationHandler = locationHandler;
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
    ByteBuf in = (ByteBuf) msg;
    String address = ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress()
        .getHostAddress();
    try {
      ByteBuffer byteBuffer = ByteBuffer.allocate(in.capacity());
      while (in.isReadable()) { // (1)
        byteBuffer.put(in.readByte());
      }

      locationHandler
          .onLocationarrived(new TrafficLog(address,
              System.currentTimeMillis(),
              byteBuffer,
              null,
              null,
              null));
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
package server;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.sf.json.JSONObject;
import protocol.Header;
import protocol.IOMessage;
import protocol.MessageType;

/**
 * 与客户端同步信息num
 * @author root
 * @time 7:14:09 PM Sep 29, 2016
 */
public class MesSyncServerHandler extends ChannelInboundHandlerAdapter{

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelActive(ctx);
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub
	    IOMessage message = (IOMessage)msg;
		if(message.getHeader() != null && message.getHeader().getType()
				                       == MessageType.ALL_RESP.value()){
			System.out.println("server receive \"ALL_RESP\" from client -- " + message);
		}else{
			if(message.getHeader() != null && message.getHeader().getType()
					                       == MessageType.CHAT_RESP.value()){
				
				System.out.println("receive MES from server -- " + message);
				
				IOMessage message_1 = new IOMessage();
				Header header = new Header();
				header.setType(MessageType.CHAT_RESP.value());
				message_1.setHeader(header);
				
				JSONObject mesJson = new JSONObject();
				mesJson.put("to", "friend_2");
				mesJson.put("num", 1);
				
				message_1.setBody(mesJson);
				ctx.writeAndFlush(message_1);
			}else{
				ctx.fireChannelRead(msg);
			}
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		super.exceptionCaught(ctx, cause);
	}
}

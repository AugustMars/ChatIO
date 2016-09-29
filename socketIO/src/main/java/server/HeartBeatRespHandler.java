package server;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.sf.json.JSONObject;
import protocol.Header;
import protocol.IOMessage;
import protocol.MessageType;

public class HeartBeatRespHandler extends ChannelInboundHandlerAdapter{

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg){
		
		IOMessage message = (IOMessage) msg;
		if(message.getHeader() != null && message.getHeader().getType()
				                       == MessageType.HEARTBEAT_REQ.value()){
			
			System.out.println("Receive client heart beat message : --> " + message);
			IOMessage heartBeat = buildHeartBeat();
			System.out.println("Send heart beat response message to client : --> " + heartBeat);
			ctx.writeAndFlush(heartBeat);
		}else{
			ctx.fireChannelRead(msg);
		}
	}
	
	private IOMessage buildHeartBeat(){
		IOMessage message = new IOMessage();
		Header header = new Header();
		header.setType(MessageType.HEARTBEAT_RESP.value());
		message.setHeader(header);
		message.setBody(new JSONObject());
		
		return message;
	}
}

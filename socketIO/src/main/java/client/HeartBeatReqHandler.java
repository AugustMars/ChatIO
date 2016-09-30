package client;

import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.ScheduledFuture;
import net.sf.json.JSONObject;
import protocol.Header;
import protocol.IOMessage;
import protocol.MessageType;

public class HeartBeatReqHandler extends ChannelInboundHandlerAdapter{

	private volatile ScheduledFuture<?> heartBeat;
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelActive(ctx);
		System.out.println("-- ** heart beat channel active ** -- ");
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg){
		
		IOMessage message = (IOMessage) msg;
		if(message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_RESP.value()){
			
			System.out.println(" heatStart-- ");
			heartBeat = ctx.executor().scheduleAtFixedRate(new heartBeatTask(ctx), 0, 5, TimeUnit.SECONDS);
			System.out.println(" heatStart-- ");
			ctx.fireChannelRead(msg);
			
		}else if(message.getHeader() != null && message.getHeader().getType() == MessageType.HEARTBEAT_RESP.value()){

			System.out.println("Client receive server heart beat message: -->" + message);
			ctx.fireChannelRead(msg);
		}else{
			ctx.fireChannelRead(msg);
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		if(heartBeat != null){
			heartBeat.cancel(true);
			heartBeat = null;
		}
		ctx.fireExceptionCaught(cause);
	}
	
	private class heartBeatTask implements Runnable{
		
		private final ChannelHandlerContext ctx;
		
		public heartBeatTask(final ChannelHandlerContext ctx) {
			// TODO Auto-generated constructor stub
		    this.ctx  = ctx;
		}
		
		public void run() {
			// TODO Auto-generated method stub
			IOMessage heartBeat = buildHeartBeat();
			System.out.println("Client send heart beat message -->" + heartBeat);
			ctx.writeAndFlush(heartBeat);
		}
		
		private IOMessage buildHeartBeat(){
			
			IOMessage message = new IOMessage();
			Header header = new Header();
			header.setType(MessageType.HEARTBEAT_REQ.value());
			message.setHeader(header);
			message.setBody(new JSONObject());
			
			return message;
		}
	}
}

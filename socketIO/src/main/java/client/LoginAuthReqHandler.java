package client;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import protocol.Header;
import protocol.IOMessage;
import protocol.MessageType;

public class LoginAuthReqHandler extends ChannelInboundHandlerAdapter{

    public static String username;
    public static String password;
	
//    public LoginAuthReqHandler(String username, String password) {
//		// TODO Auto-generated constructor stub
//        this.username = username;	
//        this.password = password;
//    }
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception{
		
		//ChatClient.ctx = ctx;
		IOMessage mes = buildLoginReq();
		System.out.println("client send -- > " + mes);
		ctx.writeAndFlush(mes);
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg){
		
		IOMessage message = (IOMessage) msg;
		System.out.println("client receive --> " + msg);
		
		if(message.getHeader() != null && message.getHeader().getType()
				                      == MessageType.LOGIN_RESP.value()){
			
		    JSONObject loginResult = message.getBody();
			System.out.println("client receive body : " + loginResult);
			
			if(loginResult.get("login").equals("failure")){
				ctx.close();
			}else{
				System.out.println("Login is Success!!");
				System.out.println("message -- get -- " + loginResult);
				
				IOMessage message_1 = new IOMessage();
				Header header = new Header();
				header.setType(MessageType.ALL_RESP.value());
				message_1.setHeader(header);
				
				JSONObject loginJson = new JSONObject();
				loginJson.put("username", username);
				loginJson.put("password", password);
				
				message_1.setBody(loginJson);
				ctx.writeAndFlush(message_1);
				
				ctx.fireChannelRead(msg);
			}
		}else{
			ctx.fireChannelRead(msg);
		}
	} 
	
	private IOMessage buildLoginReq(){
		
		System.out.println("loginReq-->");
		
		IOMessage message = new IOMessage();
		Header header = new Header();
		header.setType(MessageType.LOGIN_REQ.value());
		message.setHeader(header);
		
		JSONObject loginJson = new JSONObject();
		loginJson.put("username", username);
		loginJson.put("password", password);
		
		message.setBody(loginJson);
		return message;
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
		 throws Exception {
		// TODO Auto-generated method stub
		ctx.fireExceptionCaught(cause);
	}
	
}

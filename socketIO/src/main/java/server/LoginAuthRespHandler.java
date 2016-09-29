package server;


import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import protocol.Header;
import protocol.IOMessage;
import protocol.MessageType;

public class LoginAuthRespHandler extends ChannelInboundHandlerAdapter{

	private Map<String, Boolean> nodecheck = new ConcurrentHashMap<String, Boolean>();
	
	private String[] whitekList = {"127.0.0.1" , "192.168.1.104"};
	
	public LoginAuthRespHandler() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg){
		IOMessage message = (IOMessage) msg;

		System.out.println("server receive --> " + msg);
		
		// 如果是握手请求消息，处理，其它消息透传
		if (message.getHeader() != null
			&& message.getHeader().getType() == MessageType.LOGIN_REQ.value()) {
			
		    String nodeIndex = ctx.channel().remoteAddress().toString();
		    
		    System.out.println("remote IP --> " + nodeIndex);
		    
		    IOMessage loginResp = null;
		    // 重复登陆，拒绝
		    if (nodecheck.containsKey(nodeIndex)) {
			
		    	loginResp = buildResponse(false);
		    } else {
			
		    InetSocketAddress address = (InetSocketAddress) ctx.channel()
				                            .remoteAddress();
			String ip = address.getAddress().getHostAddress();
			
			boolean isOK = false;
//			for (String WIP : whitekList) {
//			    if (WIP.equals(ip)) {
//				isOK = true;
//				break;
//			    }
//			}
			
			JSONObject loginJson = message.getBody();
			String username = loginJson.getString("username");
			String password = loginJson.getString("password");
			if(username.equals("test") && password.equals("123"))isOK = true;
			else isOK = false;
			
			loginResp = buildResponse(isOK);
			if (isOK)
			    nodecheck.put(nodeIndex, true);
		    }
		    
		    System.out.println("The login response is : " + loginResp
			    + " body [" + loginResp.getBody() + "]");
		    ctx.writeAndFlush(loginResp);
		} else {
		    ctx.fireChannelRead(msg);
		}
	}
	
	private IOMessage buildResponse(boolean isOK) {
		IOMessage message = new IOMessage();
		Header header = new Header();
		header.setType(MessageType.LOGIN_RESP.value());
		message.setHeader(header);
		
		if(isOK){
		
		JSONObject respJson = new JSONObject();
		JSONArray array = new JSONArray();
		
		JSONObject mesJson = new JSONObject();
		String content[] = {"在吗？？", "你有没有收到信息？" , "你到底有没有收到信息？？"};
		mesJson.put("content", content);
		mesJson.put("friend", "friend_1");
		mesJson.put("num", 3);
		array.add(mesJson);

		String content_2[] = {"你好！！", "你好！！", "你好！！"};
		mesJson.put("content", content_2);
		mesJson.put("friend", "friend_2");
		mesJson.put("num", 3);
		array.add(mesJson);
		respJson.put("message", array);
		respJson.put("login", "success");
		
		message.setBody(respJson);
		}else{
			
			JSONObject respJson = new JSONObject();
			respJson.put("login", "failure");
			message.setBody(respJson);
		} 
		return message;
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		super.exceptionCaught(ctx, cause);
	}
}

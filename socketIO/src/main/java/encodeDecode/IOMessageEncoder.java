package encodeDecode;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import protocol.IOMessage;

public class IOMessageEncoder extends MessageToByteEncoder<IOMessage>{

	MarshallingEncoder marshallingEncoder;
	
	public IOMessageEncoder() throws IOException{
		// TODO Auto-generated constructor stub
		this.marshallingEncoder = new MarshallingEncoder();
	}
	
	
	@Override
	protected void encode(ChannelHandlerContext ctx, IOMessage msg, ByteBuf sendBuf) throws Exception {
		// TODO Auto-generated method stub
		
		System.out.println("encode the message --> " + msg);
		
		if(msg == null || msg.getHeader() == null){
			throw new Exception("The encode message is null");
		}
		
		//ByteBuf sendBuf = Unpooled.buffer();
		sendBuf.writeInt(msg.getHeader().getCrcCode());
		sendBuf.writeInt(msg.getHeader().getLength());
		sendBuf.writeLong(msg.getHeader().getSessionID());
		sendBuf.writeByte(msg.getHeader().getPriority());
		sendBuf.writeByte(msg.getHeader().getType());
		sendBuf.writeInt(msg.getHeader().getAttachment().size());
		
		String key = null;
		byte[] keyArray = null;
		Object value = null;
		
		for(Map.Entry<String, Object> param : msg.getHeader().getAttachment().entrySet()){
			
			key = param.getKey();
			keyArray = key.getBytes("UTF-8");
			sendBuf.writeInt(keyArray.length);
			sendBuf.writeBytes(keyArray);
			
			value = param.getValue();
			marshallingEncoder.encode(value, sendBuf);
		}
		
		key = null;
		keyArray = null;
		value = null;
		
		System.out.println("encode body --> " + msg.getBody());
		
		if(msg.getBody() != null){
			marshallingEncoder.encode(msg.getBody(), sendBuf);
		}else 
			sendBuf.writeInt(0);
		
		sendBuf.setInt(4, sendBuf.readableBytes() - 8);
		
		System.out.println("encode complete-- index -- " + sendBuf.writerIndex());
		
		//out.add(sendBuf);
	}
}

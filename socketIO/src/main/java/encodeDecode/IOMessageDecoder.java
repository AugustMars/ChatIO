package encodeDecode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jboss.marshalling.Unmarshaller;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import net.sf.json.JSONObject;
import protocol.Header;
import protocol.IOMessage;

public class IOMessageDecoder extends LengthFieldBasedFrameDecoder{

	MarshallingDecoder marshallingDecoder;
	
	public IOMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) throws IOException{
		// TODO Auto-generated constructor stub
	    super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
	    marshallingDecoder = new MarshallingDecoder();
	}
	
	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		// TODO Auto-generated method stub
		
		System.out.println("IO decode message--");
		
		ByteBuf frame = (ByteBuf)super.decode(ctx, in);
		if(frame == null)return null;
		
		IOMessage mes = new IOMessage();
		Header header = new Header();
		header.setCrcCode(frame.readInt());
		header.setLength(frame.readInt());
		header.setSessionID(frame.readLong());
		header.setPriority(frame.readByte());
		header.setType(frame.readByte());
		
		int size = frame.readInt();
		if(size > 0){
			
			Map<String, Object> attch = new HashMap<String, Object>(size);
		    int keySize = 0;
		    byte[] keyArray = null;
		    String key = null;
		    
		    for(int i = 0; i < size; i ++){
		    	keySize = frame.readInt();
		    	keyArray = new byte[keySize];
		    	in.readBytes(keyArray);
		    	key = new String(keyArray, "UTF-8");
		    	
		    	attch.put(key, marshallingDecoder.decode(frame));
		    }
		    
		    keyArray = null;
		    key = null;
		    header.setAttachment(attch);
		}
		
//		System.out.println(header);
		
		if(frame.readableBytes() > 4){
			mes.setBody((JSONObject)marshallingDecoder.decode(frame));
		}
		
		mes.setHeader(header);
		
		System.out.println("IO decode complete --> " + mes + " , body --> " + mes.getBody());
		
		return mes;
	}
}

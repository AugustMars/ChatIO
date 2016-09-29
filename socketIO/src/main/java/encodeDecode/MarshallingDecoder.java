package encodeDecode;

import java.io.IOException;

import org.jboss.marshalling.Unmarshaller;

import io.netty.buffer.ByteBuf;

public class MarshallingDecoder {

	Unmarshaller unmarshaller;
	
	public MarshallingDecoder() throws IOException{
		// TODO Auto-generated constructor stub
	    unmarshaller = MarshalleringCodecFactory.buildUnmarshaller();
	}
	
	public Object decode(ByteBuf in) throws Exception{
		
		int objectSize = in.readInt();
		ByteBuf buf = in.slice(in.readerIndex(), objectSize);
		ChannelBufferByteInput input = new ChannelBufferByteInput(buf);
		
		try{
			
			unmarshaller.start(input);
			Object obj = unmarshaller.readObject();
			unmarshaller.finish();
			in.readerIndex(in.readerIndex() + objectSize);
			
			System.out.println("marshalling --> objectSize : " + objectSize + " , " + obj);
			
			return obj;
			
		}finally{
			unmarshaller.close();
		}
	}
}

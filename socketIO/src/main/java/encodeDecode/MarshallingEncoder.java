package encodeDecode;


import java.io.IOException;

import org.jboss.marshalling.Marshaller;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class MarshallingEncoder {
     
	private static final byte[] LENGTH_PLACEHOLDER = new byte[4];
	
	Marshaller marshaller;
	
	public MarshallingEncoder() throws IOException{
		
		marshaller = MarshalleringCodecFactory.buildMarshalling();
		
	}
	
	public void encode(Object msg, ByteBuf out) throws IOException{
		
		try{
		int lengthPos = out.writerIndex();
		out.writeBytes(LENGTH_PLACEHOLDER);

		System.out.println("marshalling encode --> length_Placeholder " + out.writerIndex());
		
		ChannelBufferByteOutput output = new ChannelBufferByteOutput(out);
		marshaller.start(output);
		marshaller.writeObject(msg);
		marshaller.finish();
		out.setInt(lengthPos, out.writerIndex() - lengthPos - 4);

		System.out.println("marshalling encode --> " + msg + " , lengthPos --> " + lengthPos + " , writerIndex --> " + out.writerIndex());
		
		}finally{
			marshaller.close();
		}
	
	}
	
}

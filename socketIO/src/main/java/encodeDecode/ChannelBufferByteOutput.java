package encodeDecode;

import java.io.IOException;

import org.jboss.marshalling.ByteOutput;

import io.netty.buffer.ByteBuf;

public class ChannelBufferByteOutput implements ByteOutput{

	private final ByteBuf byteBuf;
	
	public ChannelBufferByteOutput(ByteBuf buffer) {
		// TODO Auto-generated constructor stub
		this.byteBuf = buffer;
	}
	
	public void close() throws IOException {
		// TODO Auto-generated method stub
		
	}
	
	public void flush() throws IOException {
		// TODO Auto-generated method stub
		
	}
	
	public void write(byte[] arg0) throws IOException {
		// TODO Auto-generated method stub
		byteBuf.writeBytes(arg0);
	}
	
	public void write(byte[] arg0, int arg1, int arg2) throws IOException {
		// TODO Auto-generated method stub
		byteBuf.writeBytes(arg0, arg1, arg2);
	}
	
	public void write(int arg0) throws IOException {
		// TODO Auto-generated method stub
		byteBuf.writeByte(arg0);
	}
}

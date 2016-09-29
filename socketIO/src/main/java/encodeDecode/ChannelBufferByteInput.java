package encodeDecode;

import java.io.IOException;

import org.jboss.marshalling.ByteInput;

import io.netty.buffer.ByteBuf;

public class ChannelBufferByteInput implements ByteInput{

	private final ByteBuf buffer;
	
	public ChannelBufferByteInput(ByteBuf buffer) {
		// TODO Auto-generated constructor stub
		this.buffer = buffer;
	}
	
	public void close() throws IOException {
		// TODO Auto-generated method stub
		
	}
	
	public int available() throws IOException {
		// TODO Auto-generated method stub
		return buffer.readableBytes();
	}
	
	public int read() throws IOException {
		// TODO Auto-generated method stub
		if(buffer.isReadable()){
			return buffer.readByte() & 0xff;
		}
		return -1;
	}
	
	public int read(byte[] arg0) throws IOException {
		// TODO Auto-generated method stub
		return read(arg0, 0, arg0.length);
	}
	
	public int read(byte[] array, int index, int length) throws IOException {
		// TODO Auto-generated method stub
		int avaliable = available();
		if(avaliable <= 0 )
			return -1;
		
		length = Math.min(avaliable, length);
		buffer.readBytes(array, index, length);
        return length;
	}
	
	public long skip(long skip) throws IOException {
		// TODO Auto-generated method stub
		
		long length = buffer.readableBytes();
		if(length < skip)
			skip = length;
		
		buffer.readerIndex(buffer.readerIndex() + (int)skip);
		return skip;
	}
}

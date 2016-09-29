package client;

import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import encodeDecode.IOMessageDecoder;
import encodeDecode.IOMessageEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class NettyClient {

	private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	EventLoopGroup group = new NioEventLoopGroup();
	
	public void connect(String localHost, int localPort, String username, String password)throws Exception{
		
		   try{
			   
			LoginAuthReqHandler.username = username;
			LoginAuthReqHandler.password = password;
			   
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class)
			 .option(ChannelOption.TCP_NODELAY, true)
			 .handler(new ChannelInitializer<SocketChannel>() {
				 
				protected void initChannel(SocketChannel ch) throws Exception {
					
					ch.pipeline().addLast(new IOMessageDecoder(1024*1024, 4, 4));
					ch.pipeline().addLast(new IOMessageEncoder());
					ch.pipeline().addLast(new ReadTimeoutHandler(50));
					ch.pipeline().addLast(new LoginAuthReqHandler());
					ch.pipeline().addLast(new HeartBeatReqHandler());
				}; 
			});
			ChannelFuture future = b.connect(
					new InetSocketAddress(NettyConstant.REMOTEIP, NettyConstant.PORT), 
					new InetSocketAddress(localHost, localPort)).sync();
			
			future.channel().closeFuture().sync();
		}finally{
			
			executor.execute(new Runnable() {
				
				public void run() {
					// TODO Auto-generated method stub
					try{
						TimeUnit.SECONDS.sleep(5);
						try{
							
							System.out.println("connetc again-->");
							connect(NettyConstant.REMOTEIP, NettyConstant.PORT, 
									LoginAuthReqHandler.username, LoginAuthReqHandler.password);
							
						}catch(Exception e){
							e.printStackTrace();
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			});
		}
	}
	
	public static void main(String[] args) throws Exception{
		
		new NettyClient().connect(NettyConstant.LOCALIP, NettyConstant.LOCAL_PORT, "test", "123");
	}
	
}

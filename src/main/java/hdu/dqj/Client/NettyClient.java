package hdu.dqj.Client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.net.InetSocketAddress;

/**
 * @Author dqj
 * @Date 2020/2/14
 * @Version 1.0
 * @Description
 */
public class NettyClient {
    private int PORT;   // 服务器端端口号
    private String address;  // 服务器端IP地址
    private RequestObject requestObject; // 客户端发送消息对象

    public NettyClient(String address, int PORT, RequestObject requestObject) {
        this.address = address;
        this.PORT = PORT;
        this.requestObject = requestObject;
    }

    public void run() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ObjectDecoder(1024,
                                    ClassResolvers.cacheDisabled(this.getClass().getClassLoader())));
                            ch.pipeline().addLast(new ObjectEncoder());
                            ch.pipeline().addLast(new ClientHandler(requestObject));
                        }
                    })
                    .option(ChannelOption.TCP_NODELAY, true);   // 立即发送数据

            ChannelFuture f = b.connect(address, PORT).sync();
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }
}

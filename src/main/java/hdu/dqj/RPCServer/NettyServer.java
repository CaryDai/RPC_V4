package hdu.dqj.RPCServer;

import hdu.dqj.Register.ZookeeperServiceRegister;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
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
public class NettyServer {

    private static int port = 8090; // 服务端监听端口

    public void start() throws Exception {
        // Server端的缓存
        ServerServices services = new ServerServices();
        services.registerServices();

        // 将Server端的服务注册到ZooKeeper
        ZookeeperServiceRegister serviceRegister = new ZookeeperServiceRegister();
        serviceRegister.zkClient();
        serviceRegister.serviceRegistry("hdu.dqj.RPCServer.Service.HelloService", "127.0.0.1:8090");

        NioEventLoopGroup group = new NioEventLoopGroup(); // 创建 EventLoopGroup
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)   // 设置 EventLoopGroup 用于处理所有的 Channel 的事件（Netty 限制每个Channel 都由一个 Thread 处理）
                    .channel(NioServerSocketChannel.class)        // 使用指定的 NIO 传输 Channel
                    .localAddress(new InetSocketAddress(port))    // 设置 socket 地址使用所选的端口
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 添加 ServerHandler 到 Channel 的 ChannelPipeline
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            // 解码。设置对象序列化最大长度为1M
                            // 设置线程安全的WeakReferenceMap对类加载器进行缓存
                            ch.pipeline().addLast(new ObjectDecoder(1024*1024,
                                    ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
                            // 添加对象编码器
                            ch.pipeline().addLast(new ObjectEncoder());
                            ch.pipeline().addLast(new ServerHandler());
                        }
                    });

            ChannelFuture f = b.bind().sync();            // 绑定的服务器;sync 等待服务器关闭
            f.channel().closeFuture().sync();            // 等到服务端监听端口关闭
        } finally {
            group.shutdownGracefully().sync();            // 优雅地释放线程资源
        }
    }

    public static void main(String[] args) throws Exception {
        new NettyServer().start();
    }
}

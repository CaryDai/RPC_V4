package hdu.dqj.Client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * @Author dqj
 * @Date 2020/2/14
 * @Version 1.0
 * @Description
 */
public class ClientHandler extends ChannelInboundHandlerAdapter{

    private RequestObject requestObject;

    public ClientHandler(RequestObject requestObject) {
        this.requestObject = requestObject;
    }

    // 客户端连上服务端的时候触发
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
//        System.out.println("客户端准备发消息给服务器...");
        ctx.writeAndFlush(requestObject);
    }

    // 接收服务端的数据
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            Object res = msg;
            System.out.println("Client收到消息：");
            System.out.println(res);
        } finally {
            ReferenceCountUtil.release(msg);
            ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}

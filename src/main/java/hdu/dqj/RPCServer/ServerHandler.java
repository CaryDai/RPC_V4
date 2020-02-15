package hdu.dqj.RPCServer;

import hdu.dqj.Client.RequestObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.Method;

/**
 * @Author dqj
 * @Date 2020/2/13
 * @Version 1.0
 * @Description
 */
//@ChannelHandler.Sharable    // @Sharable 标识这类的实例之间可以在 channel 里面共享
public class ServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 获取客户端发来的数据信息
        RequestObject requestObject = (RequestObject) msg;
        System.out.println("Server接受到客户端的信息 :" + requestObject.toString());

        String methodName = requestObject.getMethodName();
        String className = requestObject.getClassName();
        Class<?>[] parameterTypes = requestObject.getParameterTypes();
        Object[] params = requestObject.getParams();

        // 根据上述收到的信息，调用相应类中的方法并返回结果。
        Class serverClass = ServerServices.remoteServices.get(className);
        Method method = serverClass.getMethod(methodName, parameterTypes);
        Object result = method.invoke(serverClass.newInstance(), params);

        System.out.println("服务端处理完毕，发送结果给客户端：" + result);
        ctx.writeAndFlush(result);  // 将运行结果返回给客户端。
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();                //5
        ctx.close();                            //6
    }
}

package hdu.dqj.Client;

import hdu.dqj.Register.ZookeeperServiceDiscover;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Author dqj
 * @Date 2020/2/14
 * @Version 1.0
 * @Description
 */
public class MyInvocationHandler implements InvocationHandler {
    // 被代理的接口
    private Class target;
    private static int PORT = 0;   // 服务器端端口号
    private static String address = null;  // 服务器端IP地址

    public MyInvocationHandler(Class target) throws Exception {
        this.target = target;
        String serviceName = target.getName();
        System.out.println(serviceName);
        ZookeeperServiceDiscover serviceDiscover = new ZookeeperServiceDiscover(serviceName);
        serviceDiscover.zkClient(); // 创建服务发现的客户端
        // 服务端的地址（包括ip地址和端口号）
        String serverAddress = serviceDiscover.serviceDiscover(serviceName);
        String[] str = serverAddress.split(":");
        address = str[0];
        PORT = Integer.parseInt(str[1]);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        String className = target.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] params = args;
        RequestObject requestObject = new RequestObject(methodName, className, parameterTypes, params);
        NettyClient client = new NettyClient(address, PORT, requestObject);
        client.run();
        return null;
    }
}

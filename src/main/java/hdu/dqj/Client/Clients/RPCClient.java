package hdu.dqj.Client.Clients;

import hdu.dqj.Client.MyInvocationHandler;
import hdu.dqj.RPCServer.Service.HelloService;

import java.lang.reflect.Proxy;

/**
 * @Author dqj
 * @Date 2019/12/24
 * @Version 1.0
 * @Description 客户端的启动类
 */
public class RPCClient {
    public static void main(String[] args) throws Exception {
        MyInvocationHandler invocationHandler = new MyInvocationHandler(HelloService.class);
        // 创建代理类对象
        HelloService proxy = (HelloService) Proxy.newProxyInstance(HelloService.class.getClassLoader(),
                new Class<?>[]{HelloService.class}, invocationHandler);
        proxy.sayHi("XD");
    }
}

package hdu.dqj.Client;

import hdu.dqj.RPCServer.Service.HelloService;

import java.lang.reflect.Proxy;

/**
 * @Author dqj
 * @Date 2020/1/10
 * @Version 1.0
 * @Description 模拟第二个客户端
 */
public class RPCClient2 {
    public static void main(String[] args) throws Exception {
        MyInvocationHandler invocationHandler = new MyInvocationHandler(HelloService.class);
        // 返回代理类实例。
        HelloService proxy = (HelloService) Proxy.newProxyInstance(HelloService.class.getClassLoader(),
                new Class<?>[]{HelloService.class}, invocationHandler);
        Integer result = proxy.add(1,2);
        System.out.println("client2 receives " + result);
    }
}

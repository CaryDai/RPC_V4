package hdu.dqj.Client.Clients;

import hdu.dqj.Client.MyInvocationHandler;
import hdu.dqj.RPCServer.Service.HelloService;

import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @Author dqj
 * @Date 2019/12/24
 * @Version 1.0
 * @Description 客户端的启动类
 */
public class RPCClient {
    private static volatile boolean finish = false;
    private static final Semaphore count = new Semaphore(50);

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(50);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 50; ++i) {
            executorService.execute(() -> {
                MyInvocationHandler invocationHandler = null;
                try {
                    invocationHandler = new MyInvocationHandler(HelloService.class);
                    // 创建代理类对象
                    HelloService proxy = (HelloService) Proxy.newProxyInstance(HelloService.class.getClassLoader(),
                            new Class<?>[]{HelloService.class}, invocationHandler);
                    proxy.sayHi("XD");
                    count.acquire();
                    if (count.availablePermits() == 0) {
                        finish = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        while (!finish) {}
        long end = System.currentTimeMillis();
        long cost = end - start;
        System.out.println("所有请求处理完毕，使用时间：" + cost);
        executorService.shutdown();
    }
}

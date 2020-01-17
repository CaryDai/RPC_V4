package hdu.dqj.RPCServer;

import hdu.dqj.RPCServer.Service.HelloService;
import hdu.dqj.RPCServer.Service.HelloServiceImpl;
import hdu.dqj.Register.ZookeeperServiceRegister;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author dqj
 * @Date 2019/12/24
 * @Version 1.0
 * @Description 服务端的启动类
 */
public class RPCServer {
    public static void main(String[] args) throws Throwable {
        // 创建一个核心线程数为5，最大线程数为10，阻塞队列大小为4的线程池。
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5,10,200, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(4));
        // Server端的缓存
        ServerServices services = new ServerServices();
        services.registerServices();
        // 将Server端的服务注册到ZooKeeper
        ZookeeperServiceRegister serviceRegister = new ZookeeperServiceRegister();
        serviceRegister.zkClient();
        serviceRegister.serviceRegistry("hdu.dqj.RPCServer.Service.HelloService", "127.0.0.1:8090");
        // 建立连接
        ServerSocket listener = new ServerSocket(8090);
        System.out.println("Server等待客户端的连接...");
        // 监听请求
        while (true) {
            Socket socket = listener.accept();
            executor.execute(new ServerStub(socket));
        }
    }
}

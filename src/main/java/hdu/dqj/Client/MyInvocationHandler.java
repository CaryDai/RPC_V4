package hdu.dqj.Client;

import hdu.dqj.Register.ZookeeperServiceDiscover;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * @Author dqj
 * @Date 2019/12/24
 * @Version 1.0
 * @Description 客户端的事件处理器。
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
        ObjectOutputStream output = null;
        ObjectInputStream input = null;
        Socket socket = null;
        try {
            socket = new Socket(address, PORT);
            System.out.println("Client传递信息中...");

            // 将请求信息序列化
            output = new ObjectOutputStream(socket.getOutputStream());
            // 客户端需要把调用的方法名、方法所属的类名或接口名、方法参数类型、方法参数值发送给服务器端。
            output.writeUTF(method.getName());
            output.writeUTF(target.getName());
            output.writeObject(method.getParameterTypes());
            output.writeObject(args);
            System.out.println("Client发送信息完毕。");

            // 将响应信息反序列化
            input = new ObjectInputStream(socket.getInputStream());
            // 接收方法执行结果
            Object o = input.readObject();
            System.out.println("Client收到消息！");
            return o;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (output != null) output.close();
            if (input != null)  input.close();
            if (socket != null) socket.close();
        }
        return null;
    }
}

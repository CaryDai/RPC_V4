package hdu.dqj.RPCServer;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * @Author dqj
 * @Date 2019/12/24
 * @Version 1.0
 * @Description 服务端用来接收并处理消息的类
 */
public class ServerStub implements Runnable {

    private Socket socket;

    public ServerStub(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        ObjectOutputStream output = null;
        ObjectInputStream input = null;
        try {
            // 将收到的请求信息反序列化
            assert socket != null;
            input = new ObjectInputStream(socket.getInputStream());
            // 依次从客户端接收方法名、方法所属的类名或接口名、方法参数类型、方法参数值
            String methodName = input.readUTF();
            String className = input.readUTF();
            Class<?>[] parameterTypes = (Class<?>[]) input.readObject();
            Object[] params = (Object[]) input.readObject();
            System.out.println("Server收到：" + className + ", " + methodName);

            // 根据上述收到的信息，调用相应类中的方法并返回结果。
            Class serverClass = ServerServices.remoteServices.get(className);
            Method method = serverClass.getMethod(methodName, parameterTypes);
            Object result = method.invoke(serverClass.newInstance(), params);

            // 返回结果
            output = new ObjectOutputStream(socket.getOutputStream());
            output.writeObject(result);
            System.out.println("Server返回：" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

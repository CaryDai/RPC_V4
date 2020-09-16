package hdu.dqj.RPCServer;

import hdu.dqj.RPCServer.Service.HelloService;
import hdu.dqj.RPCServer.Service.HelloServiceImpl;

import java.util.HashMap;

/**
 * @Author dqj
 * @Date 2020/1/10
 * @Version 1.0
 * @Description Server端的缓存。
 */
public class ServerServices {
    // 用来存放Server端对象的缓存
    public static HashMap<String, Class<?>> remoteServices = new HashMap<>();

    // 把一个Server端对象放到缓存中
    private void register(Class<?> className, Class<?> remoteImpl) {
        remoteServices.put(className.getName(), remoteImpl);
    }

    // 将Server端的服务都缓存进来
    public void registerServices() {
        this.register(HelloService.class, HelloServiceImpl.class);
    }
}

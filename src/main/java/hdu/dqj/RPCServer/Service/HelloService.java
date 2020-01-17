package hdu.dqj.RPCServer.Service;

/**
 * @Author dqj
 * @Date 2019/12/3
 * @Description RPC客户端本地的一个接口（因为在同一个工程中，所以写在RPCServer包下了，实际实现时Client和Server都需要这个接口。）
 */
public interface HelloService {
    String sayHi(String name);
    Integer add(int a, int b);
}

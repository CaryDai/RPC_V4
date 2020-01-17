package hdu.dqj.RPCServer.Service;

import java.io.Serializable;

/**
 * @Author dqj
 * @Date 2019/12/3
 * @Version 1.0
 * @Description Remote端接口的实现。需要实现Serializable接口，序列化要使用。
 */
public class HelloServiceImpl implements HelloService, Serializable {
    @Override
    public String sayHi(String name) {
        return "Hi," + name;
    }

    @Override
    public Integer add(int a, int b) {
        return a + b;
    }
}

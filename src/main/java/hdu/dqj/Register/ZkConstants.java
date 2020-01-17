package hdu.dqj.Register;

/**
 * @Author dqj
 * @Date 2020/1/17
 * @Version 1.0
 * @Description Zookeeper常量
 */
class ZkConstants {

    // zk服务器列表
    static final String zkhosts = "192.168.44.128:2181";

    // zk session过期时间
    static final int SESSION_TIMEOUT = 5000;

    // zk注册的节点路径
    static final String REGISTRY_PATH = "/rpcservers";
}

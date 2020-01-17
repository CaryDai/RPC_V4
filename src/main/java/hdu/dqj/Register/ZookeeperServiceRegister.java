package hdu.dqj.Register;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 * @Author dqj
 * @Date 2020/1/10
 * @Version 1.0
 * @Description 基于Zookeeper的服务注册
 */
public class ZookeeperServiceRegister {

    private ZooKeeper zk = null;

    public void zkClient() throws IOException {
        zk = new ZooKeeper(ZkConstants.zkhosts, ZkConstants.SESSION_TIMEOUT, null);
    }

    /**
     * 向Zookeeper服务器创建服务节点，服务注册的信息就存在节点中，假设存在/rpcservers下。
     * @param serviceName
     * @param serviceAddress
     */
    public void serviceRegistry(String serviceName, String serviceAddress) throws KeeperException, InterruptedException {
        // 首先创建父节点（持久化）
        if (zk.exists(ZkConstants.REGISTRY_PATH, false) == null) {
            zk.create(ZkConstants.REGISTRY_PATH, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
        // 创建服务节点
        String result = zk.create(ZkConstants.REGISTRY_PATH + "/" + serviceName, serviceAddress.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        if (result != null) {
            System.out.println(serviceName + "注册到zookeeper成功。");
        } else {
            System.out.println("注册失败。");
        }
    }
}

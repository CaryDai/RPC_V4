package hdu.dqj.Register;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Author dqj
 * @Date 2020/1/10
 * @Version 1.0
 * @Description 基于Zookeeper的服务发现
 */
public class ZookeeperServiceDiscover {

    private ZooKeeper zk = null;
    private String serviceName = null;   // 客户端要查找的服务名
    private ArrayList<String> serverList = new ArrayList<>();

    public ZookeeperServiceDiscover(String serviceName) {
        this.serviceName = serviceName;
    }

    public void zkClient() throws IOException {
        zk = new ZooKeeper(ZkConstants.zkhosts, ZkConstants.SESSION_TIMEOUT, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                // 获取事件类型
                Event.EventType eventType = watchedEvent.getType();
                // zk 路径
                String path = watchedEvent.getPath();
                if (Event.EventType.NodeCreated == eventType) {
                    System.out.println("事件通知，新增节点" + path);
                } else if (Event.EventType.NodeDataChanged == eventType) {
                    System.out.println("事件通知，节点" + path + "被修改...");
                } else if (Event.EventType.NodeDeleted == eventType) {
                    try {
                        System.out.println("事件通知，节点" + path + "被删除...");
                        serverList.remove(path);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 从zk中获取服务器地址
     * @param serviceName
     * @return
     * @throws Exception
     */
    public String serviceDiscover(String serviceName) throws KeeperException, InterruptedException {
        // 获取rpcservers节点下的所有子节点，并注册监听
        List<String> children = zk.getChildren(ZkConstants.REGISTRY_PATH, true);
        // 服务对应的服务器列表
        serverList = new ArrayList<>();
        for (String child : children) {
            if (serviceName.equals(child)) {
                byte[] data = zk.getData(ZkConstants.REGISTRY_PATH + "/" + child, true, null);
                serverList.add(new String(data));
            }
        }
        String serverAddress = "";
        if (serverList.size() == 1) {
            serverAddress = serverList.get(0);  // 服务对应一个ip
        } else {
            // 服务对应多个ip，则随机选一个
            serverAddress = serverList.get(ThreadLocalRandom.current().nextInt(serverList.size()));
        }
        return serverAddress;
    }
}
package com.yushu.flume.entity;

import org.apache.flume.api.RpcClient;
import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.api.RpcClientFactory;
import org.apache.flume.event.EventBuilder;
import java.nio.charset.Charset;


/**
 * rpcClient实体类
 *
 * @author yushu.zhao
 * @create 2020-12-23 16:00
 */
public class MyRpcClientFacade {

    private RpcClient client;
    private String hostname;
    private int port;



    public void init(String hostname, int port) {
        //设置RPC连接
        this.hostname = hostname;
        this.port = port;
        this.client = RpcClientFactory.getDefaultInstance(hostname, port);
        System.out.println("建立连接");

    }

    public void sendDataToFlume(String data) {
        //创建一个封装示例数据的Flume事件对象
        Event event = EventBuilder.withBody(data, Charset.forName("UTF-8"));
        try {
            client.append(event);
        } catch (EventDeliveryException e) {
            //清理并重新创建客户端
            client.close();
            client = null;
            client = RpcClientFactory.getDefaultInstance(hostname, port);

        }
    }

    public void cleanUp() {
        //关闭RPC连接
        System.out.println("断开连接");
        client.close();
    }
}

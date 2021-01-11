package com.yushu.flume.util;

import com.yushu.flume.entity.MyRpcClientFacade;


/**
 *
 * 测试类
 *
 * @author yushu.zhao
 * @create 2020-12-28 15:55
 *
 */
public class TestSocketUtil {

    public static void main(String[] args){


        MyRpcClientFacade client = new MyRpcClientFacade();
        //使用远程Flume代理的主机和端口初始化客户端
        client.init("192.170.15.85", 7089);

        //发生event
//        String sampleData = "ST=91<>CN=9011<>PW=123456<>MN=88888880000001<>Flag=0";
        String sampleData = "event_id=xxx<>create_time=1609727577000<>file_name=xxx<>file_path=base64<>content_text=base64<>key_word=base64";

        System.out.println("发送数据");

        for (int i = 0; i < 1000;i++){
            client.sendDataToFlume(sampleData);
        }

        client.cleanUp();



    }
}

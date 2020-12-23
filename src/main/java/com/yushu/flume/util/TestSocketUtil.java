package com.yushu.flume.util;

import com.yushu.flume.entity.MyRpcClientFacade;


/**
 * @author yushu.zhao
 * @create 2020-12-28 15:55
 *
 */
public class TestSocketUtil {

    public static void main(String[] args){


        MyRpcClientFacade client = new MyRpcClientFacade();
        //使用远程Flume代理的主机和端口初始化客户端
        client.init("192.170.15.29", 44444);

        //发生event
        String sampleData = "ST=91;CN=9011;PW=123456;MN=88888880000001;Flag=0;CP=&&QN=20040516010101001;QnRtn=1&&";
        System.out.println("发送数据");
        client.sendDataToFlume(sampleData);
        client.cleanUp();

    }
}

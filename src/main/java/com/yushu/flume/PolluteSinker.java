package com.yushu.flume;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.yushu.flume.entity.Pollute;
import com.yushu.flume.util.FormatDataUtil;
import org.apache.flume.*;
import org.apache.flume.conf.Configurable;
import org.apache.flume.sink.AbstractSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.charset.Charset;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 污染源在线自动监控（监测）系统数据传输标准（国标）
 *
 * @author yushu.zhao
 * @create 2020-12-21 14:28
 */
public class PolluteSinker extends AbstractSink implements Configurable {

    private Logger LOG = LoggerFactory.getLogger(PolluteSinker.class);

    private String tableName;
    private String user;
    private String password;
    private String hostname;
    private String port;
    private Connection conn;
    private String databaseName;
    private PreparedStatement preparedStatement;

    @Override
    public void configure(Context context) {
        databaseName = context.getString("databaseName");
        Preconditions.checkNotNull(databaseName, "databaseName must be set!!");
        tableName = context.getString("tableName");
        Preconditions.checkNotNull(tableName, "tableName must be set!!");
        user = context.getString("user");
        Preconditions.checkNotNull(user, "user must be set!!");
        password = context.getString("password");
        Preconditions.checkNotNull(password, "password must be set!!");
        hostname = context.getString("hostname");
        Preconditions.checkNotNull(hostname, "host must be set!!");
        port = context.getString("port");
        Preconditions.checkNotNull(port, "port must be set!!");
    }

    @Override
    public Status process() throws EventDeliveryException {
        Status result = Status.READY;
        Channel channel = getChannel();
        List<String> actions = Lists.newArrayList();
        Transaction transaction = null;
        try {
            transaction = channel.getTransaction();
            Event event;
            String content;
            transaction.begin();
            event = channel.take();
            LOG.info("event:{}",event);
            if (event != null) {
                System.out.println("content++++:"+event.getBody());
                content = new String(event.getBody(), Charset.forName("UTF-8"));
                System.out.println("content"+content);
                actions.add(content);
            } else {
                result = Status.BACKOFF;
            }
            Map<String, String> map = null;
            System.out.println("actions.size() :"+actions.size() );
            if (actions.size() > 0) {
                for (String temp : actions) {
                    LOG.info("--- content : " + temp);
                    System.out.println(temp);
                    map = FormatDataUtil.getMapByData(temp);
                    List<Pollute> pollutes = new ArrayList<>();
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        Pollute pollute = new Pollute();
                        pollute.setKey(entry.getKey());
                        pollute.setValue(entry.getValue());
                        pollutes.add(pollute);
                    }
                    if (pollutes.size() > 0) {
                        preparedStatement.clearBatch();
                        for (Pollute pollute : pollutes) {
                            preparedStatement.setString(1, pollute.getKey());
                            preparedStatement.setString(2, pollute.getValue());
                            preparedStatement.setTimestamp(3,new Timestamp(System.currentTimeMillis()));
                            preparedStatement.addBatch();
                        }
                        preparedStatement.executeBatch();
                        conn.commit();
                    }
                }
            }
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("", e);
        } finally {
            if (transaction != null) {
                transaction.close();
            }

        }
        return result;
    }

    @Override
    public void start() {
        super.start();
        try {
            //调用Class.forName()方法加载驱动程序
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        String url = "jdbc:mysql://"+hostname+":3306/"+databaseName+"?allowMultiQueries=true&useUnicode=true&" +
                "characterEncoding=UTF8&zeroDateTimeBehavior=convertToNull";
        //调用DriverManager对象的getConnection()方法，获得一个Connection对象
        try {
            LOG.info("user:{},password:{}",user,password);
            conn = DriverManager.getConnection(url, user, password);
            conn.setAutoCommit(false);
            //创建一个Statement对象
            preparedStatement = conn.prepareStatement("insert into bs_pollute(`key`,`value`,date_add) values (?,?,?)");

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void stop() {
        super.stop();
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

}
package com.yushu.flume;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import com.google.common.base.Preconditions;
import org.apache.flume.Channel;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.Transaction;
import org.apache.flume.conf.Configurable;
import org.apache.flume.sink.AbstractSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * 编写自定义MysqlSink
 *
 * @author yushu.zhao
 * @create 2020-12-19 16:49
 */
public class MysqlSinker extends AbstractSink implements Configurable{

    private static final Logger logger = LoggerFactory.getLogger(MysqlSinker.class);


    private Connection connect;
    private Statement stmt;
    private String columnName;
    private String url;
    private String user;
    private String password;
    private String tableName;

    // 在整个sink结束时执行一遍
    @Override
    public synchronized void stop() {
        // TODO Auto-generated method stub
        super.stop();
    }

    // 在整个sink开始时执行一遍，用来初始化数据库连接
    @Override
    public synchronized void start() {
        // TODO Auto-generated method stub
        super.start();
        try {
            connect = DriverManager.getConnection(url, user, password);
            // 连接URL为 jdbc:mysql//服务器地址/数据库名 ，后面的2个参数分别是登陆用户名和密码
            stmt = connect.createStatement();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    // 不断循环调用，处理消息Event（本例就是插入数据库）
    public Status process() throws EventDeliveryException {

        // TODO Auto-generated method stub
        //事务，获取event什么的都是模板。仿照别的sink写就OK
        Channel ch = getChannel();
        Transaction txn = ch.getTransaction();
        Event event = null;
        txn.begin();
        while (true) {
            event = ch.take();
            if (event != null) {
                break;
            }
        }
        try {
            String rawbody = new String(event.getBody());
            //logger.error("rawbody:"+rawbody);
            String body = rawbody.split("\t")[0];
            //logger.error("spiltbody:"+body);
            if (body.split(",").length == columnName.split(",").length) {
                String sql = "insert into " + tableName + "(" + columnName + ") values(" + body + ")";
                //logger.error("sql:"+sql);
                stmt.executeUpdate(sql);
                txn.commit();
                return Status.READY;
            } else {
                txn.rollback();
                return null;
            }
        } catch (Throwable th) {
            txn.rollback();

            if (th instanceof Error) {
                throw (Error) th;
            } else {
                throw new EventDeliveryException(th);
            }
        } finally {
            txn.close();
        }

    }
    //从配置文件中读取各种属性，并进行一些非空验证
    public void configure(Context context) {
        columnName = context.getString("column_name");
        Preconditions.checkNotNull(columnName, "column_name must be set!!");
        url = context.getString("url");
        Preconditions.checkNotNull(url, "url must be set!!");
        user = context.getString("user");
        Preconditions.checkNotNull(user, "user must be set!!");
        //我的mysql没有密码。所以这里不检查密码为空
        password = context.getString("password");
        // Preconditions.checkNotNull(password, "password must be set!!");
        tableName = context.getString("tableName");
        Preconditions.checkNotNull(tableName, "tableName must be set!!");
    }









}

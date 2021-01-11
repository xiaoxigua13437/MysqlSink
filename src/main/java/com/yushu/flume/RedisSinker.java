package com.yushu.flume;


import com.google.common.base.Preconditions;
import org.apache.flume.Context;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.conf.Configurable;
import org.apache.flume.sink.AbstractSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

/**
 *
 * flume整合redis
 *
 * @author yushu.zhao
 * @create 2021-01-08
 */
public class RedisSinker extends AbstractSink implements Configurable {


    private Logger logger = LoggerFactory.getLogger(RedisSinker.class);

    private Jedis jedis;

    private String redisHost;

    private int redisPort;

    private String redisPwd;

    private String redisInstance;

    private String redisAuth;


    /**
     * 整个Sink结束的时候结束一遍
     */
    @Override
    public synchronized void start() {
        super.start();
        jedis = new Jedis(redisHost,redisPort);
        jedis.auth(redisAuth);

    }

    /**
     * 读取配置文件中redis信息,并进行非空判断
     *
     * @param context
     */
    @Override
    public void configure(Context context) {

        redisHost = context.getString("redisHost");
        Preconditions.checkNotNull(redisHost,"redisHost must be set!!");
        redisPort = context.getInteger("redisPort");
        Preconditions.checkNotNull(redisPort,"redisPort must be set!!");
        redisPwd = context.getString("redisPwd");
        Preconditions.checkNotNull(redisPwd,"redisPwd must be set!!");
        redisInstance = context.getString("redisInstance");
        redisAuth = context.getString("redisAuth");

    }



    @Override
    public Status process() throws EventDeliveryException {









        return null;
    }



}

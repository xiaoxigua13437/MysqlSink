package com.yushu.flume;

import com.google.common.collect.Lists;
import org.apache.flume.*;
import com.google.common.base.Preconditions;
import org.apache.flume.Channel;
import org.apache.flume.Context;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.Transaction;
import org.apache.flume.conf.Configurable;
import org.apache.flume.sink.AbstractSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import java.nio.charset.Charset;
import java.util.List;

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


    /**
     * 不断循环调用,处理消息Event(本例写入数据到redis)
     *
     * @return
     * @throws EventDeliveryException
     */
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
            transaction.begin();//开启事务
            event = channel.take();
            logger.info("event:{}",event);
            if (event != null){
                System.out.println("content++++:"+event.getBody());
                content = new String(event.getBody(), Charset.forName("UTF-8"));
                logger.info("content value:" + event.getBody());
                actions.add(content);

            }else {
                result = Status.BACKOFF;
            }

            if (actions.size() > 0){

                for (String temp : actions){
                    logger.info("temp value:" + temp);
                    /**
                     * 日志解析存到redis上
                     *
                     */

                }
                transaction.commit();



            }


        }catch (Throwable  e){
            logger.error("fail to show");
            transaction.rollback();

        }finally {
            transaction.close();
        }

        return result;
    }



}

package com.amu;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;

import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class RedisStringJava {
    public static void main(String[] args) {

        JedisShardInfo shardInfo = new JedisShardInfo("redis://127.0.0.1:6379");
        shardInfo.setPassword("123456");

        // Jedis jedis = new Jedis("localhost");
        Jedis jedis = new Jedis(shardInfo);
        // 测试连通性
        // System.out.println(jedis.ping());

        // 1、字符串
        // jedis.set("sky","水科院");

        // 2、Redis Java List(列表) 实例 存储数据到列表中
        /*
        jedis.lpush("site-list", "Runoob");
        jedis.lpush("site-list", "Google");
        jedis.lpush("site-list", "Taobao");
        // 获取存储的数据并输出
        List<String> list = jedis.lrange("site-list", 0 ,2);
        for(int i=0; i<list.size(); i++) {
            System.out.println("列表项为: "+list.get(i));
        }
        */

        // 3、Java Keys 实例
        // 获取数据并输出
        Set<String> keys = jedis.keys("*");
        Iterator<String> it=keys.iterator() ;
        while(it.hasNext()){
            String key = it.next();
            System.out.println(key);
        }
    }
}

package com.example;

import com.example.config.RedisConfig;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class RedisPoolTest {

    /**
     * 连接一个redis实例
     */
    @Test
    public void test() throws Exception {
        JedisPool jedisPool = RedisConfig.getPool();
        Jedis jedis = jedisPool.getResource();// 在业务操作时，从连接池获取连接

        jedis.set("name", "conanli");
        System.out.println(String.format("name: %s", jedis.get("name")));

        jedis.close();
    }

    /**
     * 连接多个redis实例，采用一致性哈稀分片
     */
    @Test
    public void testShare() throws Exception {
        ShardedJedisPool jedisPool = RedisConfig.getSharedPool();
        ShardedJedis jedis = jedisPool.getResource();// 在业务操作时，从连接池获取连接

        jedis.set("name1", "conanli1");
        System.out.println(String.format("name1: %s", jedis.get("name1")));
        jedis.set("name2", "conanli2");
        System.out.println(String.format("name2: %s", jedis.get("name2")));
        jedis.set("name3", "conanli3");
        System.out.println(String.format("name3: %s", jedis.get("name3")));
        jedis.set("name4", "conanli4");
        System.out.println(String.format("name4: %s", jedis.get("name4")));

        jedis.close();
    }

    @Test
    public void testShare2() throws Exception {
        ShardedJedisPool jedisPool = RedisConfig.getSharedPool();
        ShardedJedis jedis = jedisPool.getResource();

        System.out.println(String.format("name1: %s", jedis.get("name1")));
        System.out.println(String.format("name2: %s", jedis.get("name2")));
        System.out.println(String.format("name3: %s", jedis.get("name3")));
        System.out.println(String.format("name4: %s", jedis.get("name4")));

        Thread.sleep(10 * 1000);
        System.out.println("sleep 10 seconds, and shutdown one redis");// 抛出 JedisException: Could not get a resource from the pool

        System.out.println(String.format("name1: %s", jedis.get("name1")));
        System.out.println(String.format("name2: %s", jedis.get("name2")));
        System.out.println(String.format("name3: %s", jedis.get("name3")));
        System.out.println(String.format("name4: %s", jedis.get("name4")));

        jedis.close();
    }

}

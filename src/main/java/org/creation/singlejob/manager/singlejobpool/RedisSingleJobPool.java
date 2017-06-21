//package org.creation.singlejob.manager.singlejobpool;
//
//import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
//import org.springframework.data.redis.serializer.RedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//import redis.clients.jedis.JedisShardInfo;
//
//import org.creation.singlejob.manager.SingleJobManager;
//
//public class RedisSingleJobPool implements SingleJobPool<String , SingleJobManager> {
//    
//    JedisConnectionFactory jedisRankConnFactory;
//    
//    RedisSerializer<String> stringSerializer = new StringRedisSerializer();
//    
//    public RedisSingleJobPool(String redisUrl)
//    {
//        this.jedisRankConnFactory = new JedisConnectionFactory(new JedisShardInfo(redisUrl));
//    }
//
//    public RedisSingleJobPool(JedisConnectionFactory jedisRankConnFactory) {
//        this.jedisRankConnFactory = jedisRankConnFactory;
//    }
//
//    @Override
//    public synchronized boolean putIntoExecutorPool(String key, SingleJobManager worker) {
//        return jedisRankConnFactory.getConnection().hSet(stringSerializer.serialize("SingleJobPool"), stringSerializer.serialize(key), stringSerializer.serialize("working"));
//    }
//
//    @Override
//    public synchronized boolean removeFromJobPool(String key) {
//        return jedisRankConnFactory.getConnection().hDel(stringSerializer.serialize("SingleJobPool"), stringSerializer.serialize(key))>0;
//    }
//
//}

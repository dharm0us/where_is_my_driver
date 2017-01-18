package driverapi.service.cache;


import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

class JedisFactory {
    private static JedisPool jedisPool;
    private static JedisFactory instance;

    public JedisFactory() {
        jedisPool = new JedisPool(getPoolConfig(), "localhost");
    }

    private JedisPoolConfig getPoolConfig() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        //poolConfig.setTestOnBorrow(true);
        //poolConfig.setTestOnReturn(true);
        poolConfig.setMaxIdle(2);
// Tests whether connections are dead during idle periods
        //poolConfig.setTestWhileIdle(true);
        poolConfig.setMaxTotal(2);
        //poolConfig.setMaxWaitMillis(120000);
        return poolConfig;
    }

    public JedisPool getJedisPool() {
        //System.out.println("get pool");
        return jedisPool;
    }

    public static JedisFactory getInstance() {
        if (instance == null) {
            instance = new JedisFactory();
        }
        return instance;
    }
}

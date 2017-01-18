package driverapi.service.cache;

import java.util.Set;

import model.Driver;
import model.Location;
import redis.clients.jedis.Jedis;

public class CacheService {

    private static Jedis connection = null;

	public static String createBoxKey(Location l) {
		String prefix = "box";
		Integer latConvert = new Double(l.lat * 1000).intValue();
		Integer lngConvert = new Double(l.lng * 1000).intValue();
		return prefix + "-" + latConvert.toString() + "-" + lngConvert.toString();
	}

	public static String createDriverKey(Integer driverId) {
		return createDriverKey(driverId.toString());
	}

	static String createDriverKey(String driverId) {
		String prefix = "driver";
		return prefix + "-" + driverId;
	}

	public static void addDriverToBox(Integer driverId, String boxId) {
        Jedis jedis = JedisFactory.getInstance().getJedisPool().getResource();
        try {
            jedis.sadd(boxId, driverId.toString());
        } finally {
            JedisFactory.getInstance().getJedisPool().returnResource(jedis);
        }
	}

	public static void removeDriverFromBox(Integer driverId, String boxId) {
        Jedis jedis = JedisFactory.getInstance().getJedisPool().getResource();
        try {
            jedis.srem(boxId,driverId.toString());
        } finally {
            JedisFactory.getInstance().getJedisPool().returnResource(jedis);
        }
	}

	public static Set<String> getDriversInABox(String boxKey) throws InterruptedException {
        Jedis jedis = JedisFactory.getInstance().getJedisPool().getResource();
        try {
            return jedis.smembers(boxKey);
        } finally {
            JedisFactory.getInstance().getJedisPool().returnResource(jedis);
        }
	}

	public static String get(String key) {
        Jedis jedis = JedisFactory.getInstance().getJedisPool().getResource();
        try {
            return jedis.get(key);
        } finally {
            JedisFactory.getInstance().getJedisPool().returnResource(jedis);
        }
	}

    public static void set(String key, String val) {
        Jedis jedis = JedisFactory.getInstance().getJedisPool().getResource();
        try {
            jedis.set(key,val);
        } finally {
            JedisFactory.getInstance().getJedisPool().returnResource(jedis);
        }
    }

	public static void del(String key) {
        Jedis jedis = JedisFactory.getInstance().getJedisPool().getResource();
        try {
            jedis.del(key);
        } finally {
            JedisFactory.getInstance().getJedisPool().returnResource(jedis);
        }
	}


    public static String ping() {
        Jedis jedis = JedisFactory.getInstance().getJedisPool().getResource();
        try {
            return jedis.ping();
        } finally {
            JedisFactory.getInstance().getJedisPool().returnResource(jedis);
        }
    }
    public static void clear() {
        Jedis jedis = JedisFactory.getInstance().getJedisPool().getResource();
        try {
            jedis.flushAll();
        } finally {
            JedisFactory.getInstance().getJedisPool().returnResource(jedis);
        }
    }

    public static Driver getDriverFromCache(Integer driverId) {
        String driverKey = createDriverKey(driverId);
        String driver = get(driverKey);
        if(driver != null) {
            String[] split = driver.split("@");
            Location location = new Location(Double.parseDouble(split[0]), Double.parseDouble(split[1]));
            return new Driver(driverId.toString(),Boolean.parseBoolean(split[2]),location);
        } else {
            return null;
        }
    }
}

package com.gu.r2.common.cache;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.gu.r2.common.cache.memcached.MemcachedClient;


public class MemcachedSimpleCacheAdaptor implements SimpleCache {
	private static final Logger LOG = Logger.getLogger(MemcachedSimpleCacheAdaptor.class);
	private final MemcachedClient client;

	public MemcachedSimpleCacheAdaptor(MemcachedClient client) {
        this.client = client;
	}

	@Override
	public CacheValueWithExpiryTime getWithExpiry(Object key) {
		CacheValueWithExpiryTime cacheValueWithExpiryTime = (CacheValueWithExpiryTime) client.get(key.toString());

		if (LOG.isTraceEnabled()) {
		    if (cacheValueWithExpiryTime != null) {
			    LOG.trace(String.format("getWithExpiry(%s)[%ss] - HIT", key, cacheValueWithExpiryTime.getInstantaneousSecondsToExpiryTime()));
		    } else {
		    	LOG.trace(String.format("getWithExpiry(%s) - MISS", key));
		   	}
		}

		return cacheValueWithExpiryTime;
	}

    @Override
    public Object get(Object key) {
	    final CacheValueWithExpiryTime cacheValueWithExpiryTime = getWithExpiry(key);

	    if (cacheValueWithExpiryTime == null) {
		    if (LOG.isTraceEnabled()) {
			    LOG.trace(String.format("get(%s) - MISS", key));
		    }

		    return null;
	    }
	    if (LOG.isTraceEnabled()) {
		    LOG.trace(String.format("get(%s) - HIT", key));
	    }


	    return cacheValueWithExpiryTime.getValue();
    }

	@Override
	public void putWithExpiry(Object key, Object value, long lifetime, TimeUnit units) {
		if (LOG.isTraceEnabled()) {
			LOG.trace(String.format("putWithExpiry(%s, %d, %s)", key, lifetime, units));
		}

	    client.set(key.toString(), new CacheValueWithExpiryTime(value, lifetime, units), (int) units.toSeconds(lifetime));
	}

    @Override
    public void put(Object key, Object value) {
	    putWithExpiry(key, value, 24, TimeUnit.HOURS);
    }

    @Override
    public void remove(Object key) {
        client.remove(key.toString());
    }

}

package com.gu.cache.simplecache;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.gu.cache.memcached.MemcachedClient;


public class MemcachedSimpleCacheAdaptor implements SimpleCache {
	private static final Logger LOG = Logger.getLogger(MemcachedSimpleCacheAdaptor.class);
	private final MemcachedClient client;
	private final KeyTranslator keyTranslator;
	private final CacheValueWithExpiryTimeFactory cacheValueWithExpiryTimeFactory =
		new CacheValueWithExpiryTimeFactory();

	public MemcachedSimpleCacheAdaptor(MemcachedClient client, KeyTranslator keyTranslator) {
        this.client = client;
		this.keyTranslator = keyTranslator;
	}

	@Override
	public CacheValueWithExpiryTime getWithExpiry(Object key) {
		CacheValueWithExpiryTime cacheValueWithExpiryTime = (CacheValueWithExpiryTime) client.get(translate(key));

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

	    client.set(translate(key), cacheValueWithExpiryTimeFactory.create(value, lifetime, units), (int) units.toSeconds(lifetime));
	}

    @Override
    public void remove(Object key) {
        client.remove(translate(key));
    }

	private String translate(Object key) {
	    return keyTranslator.translate(key).toString();
	}

	@Override
	public void removeAll() {
		LOG.warn("removeAll ignored on memcached");
	}
}

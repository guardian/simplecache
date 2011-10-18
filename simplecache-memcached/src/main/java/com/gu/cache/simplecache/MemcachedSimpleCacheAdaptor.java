package com.gu.cache.simplecache;

import com.gu.cache.memcached.MemcachedClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;


public class MemcachedSimpleCacheAdaptor implements SimpleCache {
	private static final Logger LOG = LoggerFactory.getLogger(MemcachedSimpleCacheAdaptor.class);
	private final MemcachedClient client;
	private final KeyTranslator keyTranslator;
	private final CacheValueWithExpiryTimeFactory cacheValueWithExpiryTimeFactory =
		new CacheValueWithExpiryTimeFactory();


    /**
     * The behaviour of the memcached adaptor if <code>serveStaleEnabled</code> is set to true is as follows:
     * <code>putWithExpiry()</code> will tell memcached to never expire the object.
     * <code>getWithExpiry()</code> will always return the object if retrieved from memcached.
     * <code>get()</code> will only return the object if it is retrieved from memcached and it is not stale.
     */
    private boolean serveStaleEnabled = false;

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

	    if (cacheValueWithExpiryTime == null || cacheValueWithExpiryTime.isExpired()) {
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

        int memcachedExpiryTime = isServeStaleEnabled() ? 0 : (int) units.toSeconds(lifetime);

        client.set(translate(key), cacheValueWithExpiryTimeFactory.create(value, lifetime, units), memcachedExpiryTime);
        
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

    public void setServeStaleEnabled(boolean serveStaleEnabled) {
        this.serveStaleEnabled = serveStaleEnabled;
    }

    public boolean isServeStaleEnabled() {
        return serveStaleEnabled;
    }
}

package com.gu.cache.simplecache;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.apache.log4j.Logger;

import java.util.concurrent.TimeUnit;

public class EhcacheSimpleCacheAdaptor implements SimpleCache {
	private static final Logger LOG = Logger.getLogger(EhcacheSimpleCacheAdaptor.class);
    private final Ehcache cache;
    private final CacheValueWithExpiryTimeFactory cacheValueWithExpiryTimeFactory =
    	new CacheValueWithExpiryTimeFactory();

    public EhcacheSimpleCacheAdaptor(Ehcache cache) {
        this.cache = cache;
    }

	@Override
	public CacheValueWithExpiryTime getWithExpiry(Object key) {
		final Element element = cache.get(key);

		if (element == null) {
			if (LOG.isTraceEnabled()) {
				LOG.trace(String.format("getWithExpiry(%s) - MISS", key));
			}
			return null;
		}

		long expirationTime = element.getExpirationTime();
		CacheValueWithExpiryTime cacheValueWithExpiryTime = 
			cacheValueWithExpiryTimeFactory.create(element.getObjectValue(), expirationTime);

		if (LOG.isTraceEnabled()) {
			LOG.trace(String.format("getWithExpiry(%s)[%ss] - HIT", key,
                    cacheValueWithExpiryTime.getInstantaneousSecondsToExpiryTime()));
		}

		return cacheValueWithExpiryTime;
	}

    @Override
    public Object get(Object key) {
	    final CacheValueWithExpiryTime cacheValueWithExpiryTime = getWithExpiry(key);

	    if (cacheValueWithExpiryTime == null) {
		    return null;
	    }

	    return cacheValueWithExpiryTime.getValue();
    }

    @Override
    public void putWithExpiry(Object key, Object value, long lifetime, TimeUnit units) {
	    if (LOG.isTraceEnabled()) {
		    LOG.trace(String.format("putWithExpiry(%s, %d, %s)", key, lifetime, units));
	    }
        Element element = new Element(key, value);
        element.setTimeToLive((int) units.toSeconds(lifetime));
        cache.put(element);
    }

    @Override
    public void remove(Object key) {
	    if (LOG.isTraceEnabled()) {
		    LOG.trace(String.format("remove(%s)", key));
	    }

        cache.remove(key);
    }
}

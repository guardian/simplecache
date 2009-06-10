package com.gu.cache.simplecache;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.google.common.collect.MapMaker;

public class SoftReferenceSimpleCache implements SimpleCache {
	private static final Logger LOG = Logger.getLogger(SoftReferenceSimpleCache.class); 

    private final ConcurrentMap<Object, CacheValueWithExpiryTime> cache;
	private CacheValueWithExpiryTimeFactory cacheValueWithExpiryTimeFactory = new CacheValueWithExpiryTimeFactory();

    public SoftReferenceSimpleCache() {
        cache = new MapMaker()
                .concurrencyLevel(50)
                .softValues()
                .makeMap();
    }

    @Override
    public Object get(Object key) {
        CacheValueWithExpiryTime cacheValueWithExpiryTime = getWithExpiry(key);
        
        if (cacheValueWithExpiryTime == null) {
        	return null;
        }

		return cacheValueWithExpiryTime.getValue();
    }

    @Override
    public CacheValueWithExpiryTime getWithExpiry(Object key) {
    	CacheValueWithExpiryTime cacheValueWithExpiryTime = cache.get(key);
    	
    	if (cacheValueWithExpiryTime == null) {
        	if (LOG.isTraceEnabled()) {
        		LOG.trace("getWithExpiry(" + key + ") - MISS");
        	}
    		return null;
    	}
    	
    	if (cacheValueWithExpiryTime.isExpired()) {
        	if (LOG.isDebugEnabled()) {
        		LOG.debug("getWithExpiry(" + key + ") - removing and ignoring expired entry");
        	}
    		cache.remove(key);
    		return null;
    	}
    	
    	if (LOG.isTraceEnabled()) {
    		LOG.trace("getWithExpiry(" + key + ") - HIT");
    	}
    	
    	return cacheValueWithExpiryTime;
    }

    @Override
    public void putWithExpiry(Object key, Object value, long lifetime, TimeUnit units) {
    	CacheValueWithExpiryTime cacheValue = cacheValueWithExpiryTimeFactory.create(value, lifetime, units);
        cache.put(key, cacheValue);

    	if (LOG.isTraceEnabled()) {
    		LOG.trace(String.format("putWithExpiry(%s) => '%s'", key, value) );
    	}

    }

    @Override
    public void remove(Object key) {
    	if (LOG.isTraceEnabled()) {
    		LOG.trace(String.format("remove(%s)", key) );
    	}
        cache.remove(key);
    }

	public void setCacheValueWithExpiryFactory(CacheValueWithExpiryTimeFactory cacheValueWithExpiryTimeFactory) {
		this.cacheValueWithExpiryTimeFactory = cacheValueWithExpiryTimeFactory;
	}

}

package com.gu.cache.simplecache;

import com.google.common.collect.MapMaker;
import com.gu.cache.simplecache.statistics.Statistics;
import com.gu.cache.simplecache.statistics.StatisticsCounter;
import com.gu.cache.simplecache.statistics.StatisticsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

public class SoftReferenceSimpleCache implements SimpleCache, StatisticsProvider {
	private static final Logger LOG = LoggerFactory.getLogger(SoftReferenceSimpleCache.class);

    private final ConcurrentMap<Object, CacheValueWithExpiryTime> cache;

	private CacheValueWithExpiryTimeFactory cacheValueWithExpiryTimeFactory = new CacheValueWithExpiryTimeFactory();
	private String name;
    private StatisticsCounter count = new StatisticsCounter();

    public SoftReferenceSimpleCache() {
        cache = new MapMaker()
                .concurrencyLevel(50)
                .softValues()
                .makeMap();
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

            count.miss();
    		return null;
    	}
    	
    	if (cacheValueWithExpiryTime.isExpired()) {
        	if (LOG.isDebugEnabled()) {
        		LOG.debug("getWithExpiry(" + key + ") - MISS (removing and ignoring expired entry)");
        	}
    		cache.remove(key);

            count.miss();
    		return null;
    	}
    	
    	if (LOG.isTraceEnabled()) {
    		LOG.trace("getWithExpiry(" + key + ") - HIT");
    	}

        count.hit();

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

    @Override
    public void removeAll() {
    	LOG.debug("removeAll");

    	cache.clear();
    }

	public void setCacheValueWithExpiryFactory(CacheValueWithExpiryTimeFactory cacheValueWithExpiryTimeFactory) {
		this.cacheValueWithExpiryTimeFactory = cacheValueWithExpiryTimeFactory;
	}

    @Override
    public Statistics getStatistics() {
        return count.asStatistics(cache.size());
    }
}

package com.gu.cache.simplecache;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TwoLevelSimpleCacheAggregator implements SimpleCache {
	private static final Logger LOG = LoggerFactory.getLogger(TwoLevelSimpleCacheAggregator.class);

	private final SimpleCache firstLevelCache;
    private final SimpleCache secondLevelCache;
    private int minTtlForPromotion = 5;

	public TwoLevelSimpleCacheAggregator(SimpleCache firstLevelCache, SimpleCache secondLevelCache) {
        this.firstLevelCache = firstLevelCache;
        this.secondLevelCache = secondLevelCache;
    }

	public int getMinTtlForPromotion() {
		return minTtlForPromotion;
	}

	public void setMinTtlForPromotion(int minTtlForPromotion) {
		this.minTtlForPromotion = minTtlForPromotion;
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

        firstLevelCache.putWithExpiry(key, value, lifetime, units);
        secondLevelCache.putWithExpiry(key, value, lifetime, units);
    }

    @Override
    public void remove(Object key) {
	    if (LOG.isTraceEnabled()) {
		    LOG.trace(String.format("remove(%s)", key));
	    }

	    secondLevelCache.remove(key);
        firstLevelCache.remove(key);
    }

	@Override
	public void removeAll() {
	    LOG.debug("removeAll()");

	    secondLevelCache.removeAll();
		firstLevelCache.removeAll();
	}

	@Override
	public CacheValueWithExpiryTime getWithExpiry(Object key) {
		CacheValueWithExpiryTime cacheValueWithExpiryTime = firstLevelCache.getWithExpiry(key);

		if (cacheValueWithExpiryTime != null) {
			if (LOG.isTraceEnabled()) {
				LOG.trace(String.format("getWithExpiry(%s)[%ss] - HIT 1st", key, cacheValueWithExpiryTime.getInstantaneousSecondsToExpiryTime()));
			}

            return cacheValueWithExpiryTime;
        }
		if (LOG.isTraceEnabled()) {
			LOG.trace(String.format("getWithExpiry(%s) - MISS 1st", key));
		}


        cacheValueWithExpiryTime = secondLevelCache.getWithExpiry(key);

	    if (cacheValueWithExpiryTime != null) {
		    if (LOG.isTraceEnabled()) {
			    LOG.trace(String.format("getWithExpiry(%s)[%ss] - HIT 2nd", key, cacheValueWithExpiryTime.getInstantaneousSecondsToExpiryTime()));
		    }

		    // Hazard : expiration clock is still ticking on the object
		    long relativeExpiryTimeInSeconds = cacheValueWithExpiryTime.getInstantaneousSecondsToExpiryTime();

		    if (relativeExpiryTimeInSeconds < minTtlForPromotion) {
		    	if (LOG.isDebugEnabled()) {
		    		LOG.debug("Decayed expiry time " + relativeExpiryTimeInSeconds + " for key(" +
		    			key + ") from second level cache. Reporting MISS.");
		    	}

		    	return null;
		    }

		    firstLevelCache.putWithExpiry(key, cacheValueWithExpiryTime.getValue(), relativeExpiryTimeInSeconds, TimeUnit.SECONDS);
	    } else {
		    if (LOG.isTraceEnabled()) {
			    LOG.trace(String.format("getWithExpiry(%s) - MISS 2nd", key));
		    }
	    }

	    return cacheValueWithExpiryTime;
	}

}

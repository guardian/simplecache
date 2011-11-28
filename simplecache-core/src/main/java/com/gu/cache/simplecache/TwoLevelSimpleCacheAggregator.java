package com.gu.cache.simplecache;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class TwoLevelSimpleCacheAggregator implements SimpleCache {
	private static final Logger LOG = Logger.getLogger(TwoLevelSimpleCacheAggregator.class);

	private final SimpleCache firstLevelCache;
    private final SimpleCache secondLevelCache;
    private boolean serveStaleEnabled = false;
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
    public boolean isServeStaleEnabled() {
        return serveStaleEnabled;
    }

    @Override
    public void setServeStaleEnabled(boolean serveStaleEnabled) {
        this.serveStaleEnabled = serveStaleEnabled;
        firstLevelCache.setServeStaleEnabled(serveStaleEnabled);
        secondLevelCache.setServeStaleEnabled(serveStaleEnabled);
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
        if (!serveStaleEnabled) {
            if (LOG.isTraceEnabled()) {
                LOG.trace(String.format("remove(%s)", key));
            }

            secondLevelCache.remove(key);
            firstLevelCache.remove(key);
        } else {
            LOG.debug(String.format("remove(%s) ignored in serve stale mode", key));
        }
    }

	@Override
	public void removeAll() {
        if (!serveStaleEnabled) {
            LOG.debug("removeAll()");

            secondLevelCache.removeAll();
            firstLevelCache.removeAll();
        } else {
            LOG.debug("removeAll ignored in serve stale mode");
        }
	}

    private CacheValueWithExpiryTime getFromFirstLevelWithExpiry(Object key) {
        CacheValueWithExpiryTime cacheValueWithExpiryTime = firstLevelCache.getWithExpiry(key);
        if (LOG.isTraceEnabled()) {
            if (cacheValueWithExpiryTime != null) {
                LOG.trace(String.format("getWithExpiry(%s)[%ss] - HIT 1st", key,
                        cacheValueWithExpiryTime.getInstantaneousSecondsToExpiryTime()));
            } else {
                LOG.trace(String.format("getWithExpiry(%s) - MISS 1st", key));
            }
        }

        return cacheValueWithExpiryTime;
    }

    private CacheValueWithExpiryTime getFromSecondLevelWithExpiry(Object key) {
        CacheValueWithExpiryTime cacheValueWithExpiryTime = secondLevelCache.getWithExpiry(key);
        if (LOG.isTraceEnabled()) {
            if (cacheValueWithExpiryTime != null) {
                LOG.trace(String.format("getWithExpiry(%s)[%ss] - HIT 2nd", key,
                        cacheValueWithExpiryTime.getInstantaneousSecondsToExpiryTime()));

            } else {
                LOG.trace(String.format("getWithExpiry(%s) - MISS 2nd", key));
            }
        }

        return cacheValueWithExpiryTime;
    }

    private CacheValueWithExpiryTime filterDecayedAndPromote(Object key, CacheValueWithExpiryTime cacheValueWithExpiryTime) {
        // Hazard : expiration clock is still ticking on the object
        long relativeExpiryTimeInSeconds = cacheValueWithExpiryTime.getInstantaneousSecondsToExpiryTime();

        // Filter decayed
        if (!serveStaleEnabled && relativeExpiryTimeInSeconds < minTtlForPromotion) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Decayed expiry time " + relativeExpiryTimeInSeconds + " for key(" +
                        key + ") from second level cache. Reporting MISS.");
            }

            return null;
        }

        // Promote
        firstLevelCache.putWithExpiry(key, cacheValueWithExpiryTime.getValue(), relativeExpiryTimeInSeconds, TimeUnit.SECONDS);

        return cacheValueWithExpiryTime;
    }


    @Override
	public CacheValueWithExpiryTime getWithExpiry(final Object key) {
        CacheValueWithExpiryTime cacheValueWithExpiryTime = getFromFirstLevelWithExpiry(key);

        if (cacheValueWithExpiryTime == null) {
            cacheValueWithExpiryTime = getFromSecondLevelWithExpiry(key);

            if (cacheValueWithExpiryTime != null) {
                cacheValueWithExpiryTime = filterDecayedAndPromote(key, cacheValueWithExpiryTime);
            }
        }

        return cacheValueWithExpiryTime;
	}

}

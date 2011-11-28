package com.gu.cache.simplecache;

import com.gu.cache.simplecache.statistics.StatisticsCounter;
import org.apache.log4j.Logger;

import java.util.concurrent.TimeUnit;

public abstract class AbstractSimpleCache implements SimpleCache {
	private static final Logger LOG = Logger.getLogger(AbstractSimpleCache.class);

	private String name;
    private boolean serveStaleEnabled = false;
    private StatisticsCounter count = new StatisticsCounter();
    private CacheValueWithExpiryTimeFactory cacheValueWithExpiryTimeFactory = new CacheValueWithExpiryTimeFactory();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    @Override
    public boolean isServeStaleEnabled() {
        return serveStaleEnabled;
    }

    @Override
    public void setServeStaleEnabled(boolean serveStaleEnabled) {
        this.serveStaleEnabled = serveStaleEnabled;
    }

    protected abstract Object getDirect(Object key);

    protected abstract void removeDirect(Object key);

    protected abstract void putDirect(Object key, Object cacheValue);

    protected abstract void removeAllDirect();

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
    	CacheValueWithExpiryTime cacheValueWithExpiryTime = (CacheValueWithExpiryTime) getDirect(key);
    	
    	if (cacheValueWithExpiryTime == null) {
        	if (LOG.isTraceEnabled()) {
        		LOG.trace("getWithExpiry(" + key + ") - MISS");
        	}
            count.miss();

    		return null;
    	}

    	if (cacheValueWithExpiryTime.isExpired() && !serveStaleEnabled) {
        	if (LOG.isDebugEnabled()) {
        		LOG.debug("getWithExpiry(" + key + ") - MISS (removing and ignoring expired entry)");
        	}
    		// We don't removeDirect(key) here because the miss will trigger it to be updated and
            // this recent read means the stale content would have locality anyway. We trust the
            // underlying cache implementation to handle eviction itself.
            count.miss();

    		return null;
    	}

    	if (cacheValueWithExpiryTime.isExpired() && serveStaleEnabled) {
        	if (LOG.isDebugEnabled()) {
        		LOG.debug("getWithExpiry(" + key + ") - HIT (serving stale)");
        	}
            count.serveStale();
            count.hit();

    	    return cacheValueWithExpiryTime;
    	}

    	if (LOG.isTraceEnabled()) {
            LOG.trace(String.format("getWithExpiry(%s)[%ss] - HIT", key, cacheValueWithExpiryTime.getInstantaneousSecondsToExpiryTime()));
    	}
        count.hit();

    	return cacheValueWithExpiryTime;
    }

    @Override
    public void putWithExpiry(Object key, Object value, long lifetime, TimeUnit units) {
    	CacheValueWithExpiryTime cacheValue = cacheValueWithExpiryTimeFactory.create(value, lifetime, units);
        putDirect(key, cacheValue);

    	if (LOG.isTraceEnabled()) {
    		LOG.trace(String.format("putWithExpiry(%s) => '%s'", key, value) );
    	}
        count.write();
    }

    @Override
    public void remove(Object key) {
        if (!serveStaleEnabled) {
            if (LOG.isTraceEnabled()) {
                LOG.trace(String.format("remove(%s)", key) );
            }
            removeDirect(key);
            count.remove();
        } else {
            LOG.debug(String.format("remove(%s) ignored in serve stale mode", key));
        }
    }

    @Override
    public void removeAll() {
        if (!serveStaleEnabled) {
            LOG.debug("removeAll");
            removeAllDirect();
            count.removeAll();
        } else {
            LOG.debug("removeAll ignored in serve stale mode");
        }
    }

    protected StatisticsCounter getStatisticsCounter() {
        return count;
    }
}

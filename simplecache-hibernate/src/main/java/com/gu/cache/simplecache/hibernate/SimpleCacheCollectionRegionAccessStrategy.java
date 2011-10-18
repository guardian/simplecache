package com.gu.cache.simplecache.hibernate;

import java.util.concurrent.TimeUnit;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.CollectionRegion;
import org.hibernate.cache.access.CollectionRegionAccessStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gu.cache.simplecache.SimpleCache;

public class SimpleCacheCollectionRegionAccessStrategy extends SimpleCacheDataRegionAccessStrategy implements CollectionRegionAccessStrategy {
    private static final Logger LOG = LoggerFactory.getLogger(SimpleCacheCollectionRegionAccessStrategy.class);

    private final SimpleCacheCollectionRegion collectionRegion;

    public SimpleCacheCollectionRegionAccessStrategy(SimpleCacheCollectionRegion collectionRegion) {
        this.collectionRegion = collectionRegion;
    }

	private SimpleCache getCache() {
        return collectionRegion.getCache();
    }

    @Override
    public CollectionRegion getRegion() {
        return collectionRegion;
    }

    @Override
    public Object get(Object key, long txTimestamp) throws CacheException {
    	if (LOG.isTraceEnabled()) {
    		LOG.trace("get {} {}", key, txTimestamp);
    	}
        return getCache().get(key);
    }

    @Override
    public boolean putFromLoad(Object key, Object value, long txTimestamp, Object version) throws CacheException {
    	if (LOG.isTraceEnabled()) {
    		LOG.trace("putFromLoad {} => {}", key, value);
    	}
    	put(key, value);
	    return true;
    }

	private void put(Object key, Object value) {
		getCache().putWithExpiry(key, value, collectionRegion.getTtlSeconds(), TimeUnit.SECONDS);
	}

	@Override
    public boolean putFromLoad(Object key, Object value, long txTimestamp, Object version, boolean minimalPutOverride) throws CacheException {
    	if (LOG.isTraceEnabled()) {
    		LOG.trace("putFromLoad (with minimal put) {} => {} ", key, value);
    	}
		put(key, value);
		return true;
    }

    @Override
    public void remove(Object key) throws CacheException {
    	if (LOG.isTraceEnabled()) {
    		LOG.trace("remove {}", key);
    	}
        getCache().remove(key);
    }

    @Override
    public void evict(Object key) throws CacheException {
    	if (LOG.isTraceEnabled()) {
    		LOG.trace("evict {}", key);
    	}
        getCache().remove(key);
    }
}

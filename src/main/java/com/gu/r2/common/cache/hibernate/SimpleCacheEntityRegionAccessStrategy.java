package com.gu.r2.common.cache.hibernate;

import java.util.concurrent.TimeUnit;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.EntityRegion;
import org.hibernate.cache.access.EntityRegionAccessStrategy;
import org.hibernate.cache.access.SoftLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gu.r2.common.cache.SimpleCache;

public class SimpleCacheEntityRegionAccessStrategy extends SimpleCacheDataRegionAccessStrategy implements EntityRegionAccessStrategy {
    private static final Logger LOG = LoggerFactory.getLogger(SimpleCacheEntityRegionAccessStrategy.class);

    private final SimpleCacheEntityRegion entityRegion;

    public SimpleCacheEntityRegionAccessStrategy(SimpleCacheEntityRegion entityRegion) {
        this.entityRegion = entityRegion;
    }

    private SimpleCache getCache() {
        return entityRegion.getCache();
    }

    @Override
    public Object get(Object key, long txTimestamp) throws CacheException {
        if (LOG.isTraceEnabled()) {
        	LOG.trace("get {}", key);
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

    @Override
    public boolean putFromLoad(Object key, Object value, long txTimestamp, Object version, boolean minimalPutOverride) throws CacheException {
        if (LOG.isTraceEnabled()) {
        	LOG.trace("putFromLoad (with minimal put) {} => {} ", key, value);
        }
        put(key, value);
        return true;
    }

    @Override
    public boolean afterInsert(Object key, Object value, Object version) throws CacheException {
        if (LOG.isTraceEnabled()) {
        	LOG.trace("afterInsert {} => {}", key, value);
        }
        put(key, value);
        return true;
    }

	private void put(Object key, Object value) {
		getCache().putWithExpiry(key, value, entityRegion.getTtlSeconds(), TimeUnit.SECONDS);
	}

	@Override
    public boolean afterUpdate(Object key, Object value, Object currentVersion, Object previousVersion, SoftLock lock) throws CacheException {
        if (LOG.isTraceEnabled()) {
        	LOG.trace("afterUpdate {} => {}", key, value);
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

    @Override
    public EntityRegion getRegion() {
        return entityRegion;
    }
}

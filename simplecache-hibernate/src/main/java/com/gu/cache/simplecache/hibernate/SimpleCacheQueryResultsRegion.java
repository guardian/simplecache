package com.gu.cache.simplecache.hibernate;

import java.util.concurrent.TimeUnit;

import org.hibernate.cache.CacheDataDescription;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.QueryResultsRegion;
import org.apache.log4j.Logger;

import com.gu.cache.simplecache.SimpleCache;

public class SimpleCacheQueryResultsRegion extends SimpleCacheDataRegion implements QueryResultsRegion {
    private static final Logger LOG = Logger.getLogger(SimpleCacheQueryResultsRegion.class);

    public SimpleCacheQueryResultsRegion(String name, CacheDataDescription metadata, SimpleCache cache, int ttlSeconds) {
        super(name, metadata, cache, ttlSeconds);
    }

    @Override
    public Object get(Object key) throws CacheException {
        if (LOG.isTraceEnabled()) {
        	LOG.trace("get " + key);
        }
        return getCache().get(key);
    }

    @Override
    public void put(Object key, Object value) throws CacheException {
        if (LOG.isTraceEnabled()) {
        	LOG.trace(String.format("put %s => %s", key, value));
        }
        getCache().putWithExpiry(key, value, getTtlSeconds(), TimeUnit.SECONDS);
    }

    @Override
    public void evict(Object key) throws CacheException {
        if (LOG.isTraceEnabled()) {
        	LOG.trace("evict " + key);
        }
        getCache().remove(key);
    }

    @Override
    public void evictAll() throws CacheException {
        LOG.warn("evictAll ignored");
    }
}

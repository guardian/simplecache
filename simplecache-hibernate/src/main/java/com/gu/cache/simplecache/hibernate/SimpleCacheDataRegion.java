package com.gu.cache.simplecache.hibernate;

import java.util.Collections;
import java.util.Map;

import org.hibernate.cache.CacheDataDescription;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.TransactionalDataRegion;

import com.gu.cache.simplecache.SimpleCache;

public class SimpleCacheDataRegion implements TransactionalDataRegion {
    private final String name;
	private final int ttlSeconds;
	private final CacheDataDescription metadata;
    private final SimpleCache cache;

    public SimpleCacheDataRegion(String name, CacheDataDescription metadata, SimpleCache cache, int ttlSeconds) {
        this.cache = cache;
        this.metadata = metadata;
        this.name = name;
	    this.ttlSeconds = ttlSeconds;
    }

	public int getTtlSeconds() {
		return ttlSeconds;
	}

	@Override
    public boolean isTransactionAware() {
        return false;
    }

    @Override
    public CacheDataDescription getCacheDataDescription() {
        return metadata;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void destroy() throws CacheException {
    }

    @Override
    public long getSizeInMemory() {
        return -1;
    }

    @Override
    public long getElementCountInMemory() {
        return -1;
    }

    @Override
    public long getElementCountOnDisk() {
        return -1;
    }

    @SuppressWarnings("unchecked")
	@Override
    public Map toMap() {
        return Collections.emptyMap();
    }

    @Override
    public long nextTimestamp() {
        return System.currentTimeMillis() / 100;
    }

    @Override
    public int getTimeout() {
        return 300;
    }

    public SimpleCache getCache() {
        return cache;
    }
}

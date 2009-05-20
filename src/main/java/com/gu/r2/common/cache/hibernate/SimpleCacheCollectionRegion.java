package com.gu.r2.common.cache.hibernate;

import org.hibernate.cache.CacheDataDescription;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.CollectionRegion;
import org.hibernate.cache.access.AccessType;
import org.hibernate.cache.access.CollectionRegionAccessStrategy;

import com.gu.r2.common.cache.SimpleCache;


public class SimpleCacheCollectionRegion extends SimpleCacheDataRegion implements CollectionRegion {
    public SimpleCacheCollectionRegion(String name, CacheDataDescription metadata, SimpleCache cache, int ttlSeconds) {
        super(name, metadata, cache, ttlSeconds);
    }

	@Override
    public CollectionRegionAccessStrategy buildAccessStrategy(AccessType accessType) throws CacheException {
        return new SimpleCacheCollectionRegionAccessStrategy(this);
    }
}

package com.gu.r2.common.cache.hibernate;

import org.hibernate.cache.CacheDataDescription;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.EntityRegion;
import org.hibernate.cache.access.AccessType;
import org.hibernate.cache.access.EntityRegionAccessStrategy;

import com.gu.r2.common.cache.SimpleCache;

public class SimpleCacheEntityRegion extends SimpleCacheDataRegion implements EntityRegion {

    public SimpleCacheEntityRegion(String name, CacheDataDescription metadata, SimpleCache cache, int ttlSeconds) {
        super(name, metadata, cache, ttlSeconds);
    }

    @Override
    public EntityRegionAccessStrategy buildAccessStrategy(AccessType accessType) throws CacheException {
        return new SimpleCacheEntityRegionAccessStrategy(this);
    }

}

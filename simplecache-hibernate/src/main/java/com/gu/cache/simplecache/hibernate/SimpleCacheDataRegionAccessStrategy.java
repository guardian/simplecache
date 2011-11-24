package com.gu.cache.simplecache.hibernate;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.access.SoftLock;
import org.apache.log4j.Logger;

public class SimpleCacheDataRegionAccessStrategy {
    private static final Logger LOG = Logger.getLogger(SimpleCacheDataRegionAccessStrategy.class);

    public boolean insert(Object key, Object value, Object version) throws CacheException {
        // we don't want to update the cache on insert, because the transaction may still roll back
        return false;
    }

    public boolean update(Object key, Object value, Object currentVersion, Object previousVersion) throws CacheException {
        // we don't want to update the cache on insert, because the transaction may still roll back
        return false;
    }

    public void removeAll() throws CacheException {
        LOG.warn("removeAll not supported");
    }

    public void evictAll() throws CacheException {
        LOG.warn("evictAll not supported");
    }

    /////////////////////////////////////////////////////////////////
    //
    // No locking supported
    //
    /////////////////////////////////////////////////////////////////
    public SoftLock lockItem(Object key, Object version) throws CacheException {
        return null;
    }

    public SoftLock lockRegion() throws CacheException {
        return null;
    }

    public void unlockItem(Object key, SoftLock lock) throws CacheException {
    }

    public void unlockRegion(SoftLock lock) throws CacheException {
    }
}

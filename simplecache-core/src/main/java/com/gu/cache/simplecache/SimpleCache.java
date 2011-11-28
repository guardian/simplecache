package com.gu.cache.simplecache;

import java.util.concurrent.TimeUnit;

public interface SimpleCache {
    Object get(Object key);
	CacheValueWithExpiryTime getWithExpiry(Object key);

	/*
	 * No "put" (without expiry) deliberately.  It's *your* responsibility,
	 * as caller, to explicitly specify the lifetime of objects you add.
	 * 
	 * Trust us.  This works far better than having a default lifetime.
	 * 
	 * If you don't know what lifetime to specify, you're probably haven't 
	 * thought through your caching requirements properly. 
	 */
    void putWithExpiry(Object key, Object value, long lifetime, TimeUnit units);

    void remove(Object key);

    void removeAll();

    boolean isServeStaleEnabled();
    void setServeStaleEnabled(boolean serveStaleEnabled);
}

package com.gu.r2.common.cache;

import java.util.concurrent.TimeUnit;

public interface SimpleCache {
    Object get(Object key);
	CacheValueWithExpiryTime getWithExpiry(Object key);

    void put(Object key, Object value);
    void putWithExpiry(Object key, Object value, long lifetime, TimeUnit units);

    void remove(Object key);

}

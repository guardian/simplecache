package com.gu.cache.simplecache;

import java.util.concurrent.TimeUnit;

public class NullSimpleCache implements SimpleCache {

	@Override
	public Object get(Object key) {
		return null;
	}

	@Override
	public CacheValueWithExpiryTime getWithExpiry(Object key) {
		return null;
	}

	@Override
	public void putWithExpiry(Object key, Object value, long lifetime, TimeUnit units) {
	}

	@Override
	public void remove(Object key) {
	}

	@Override
	public void removeAll() {
	}

}

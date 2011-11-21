package com.gu.cache.simplecache;

import java.util.concurrent.TimeUnit;

public class CacheValueWithExpiryTimeFactory {

	public CacheValueWithExpiryTime create(Object value, long duration, TimeUnit timeUnit) {
		return create(value, Clock.currentTimeMillis() + timeUnit.toMillis(duration));
	}

	public CacheValueWithExpiryTime create(Object value, long expirationTime) {
		return new CacheValueWithExpiryTime(value, expirationTime);
	}
}

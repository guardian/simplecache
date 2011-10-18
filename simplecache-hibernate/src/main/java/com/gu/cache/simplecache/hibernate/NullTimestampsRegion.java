package com.gu.cache.simplecache.hibernate;

import java.util.Map;

import org.hibernate.cache.CacheException;
import org.hibernate.cache.TimestampsRegion;

/*
 * Our caching strategy does not use Hibernate timestamps.  We want a dummy TimestampsRegion
 * because otherwise our JBoss config will back the default TimestampsRegion with Memcached,
 * creating significant Memcached contention, traffic, lag looking up nonexistent timestamps.
 */
public class NullTimestampsRegion implements TimestampsRegion {

	@Override
	public void evict(Object key) throws CacheException {
	}

	@Override
	public void evictAll() throws CacheException {
	}

	@Override
	public Object get(Object key) throws CacheException {
		return null;
	}

	@Override
	public void put(Object key, Object value) throws CacheException {
	}

	@Override
	public void destroy() throws CacheException {
	}

	@Override
	public long getElementCountInMemory() {
		return 0;
	}

	@Override
	public long getElementCountOnDisk() {
		return 0;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public long getSizeInMemory() {
		return 0;
	}

	@Override
	public int getTimeout() {
		return 0;
	}

	@Override
	public long nextTimestamp() {
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map toMap() {
		return null;
	}
}

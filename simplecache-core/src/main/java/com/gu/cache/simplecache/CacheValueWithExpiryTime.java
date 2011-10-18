package com.gu.cache.simplecache;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class CacheValueWithExpiryTime implements Serializable {
	private Object value;
	private long absoluteExpiryTime;

	// Use CacheValueWithExpiryTimeFactory to create these
	CacheValueWithExpiryTime(Object value, long absoluteExpiryTime) {
		this.value = value;
		this.absoluteExpiryTime = absoluteExpiryTime;
	}

	public Object getValue() {
		return value;
	}
	
	public boolean isExpired() {
		return absoluteExpiryTime <= System.currentTimeMillis();
	}

	public long getInstantaneousSecondsToExpiryTime() {
		final long nowInMillis = System.currentTimeMillis();
		final long relativeExpiryTimeInMillis = absoluteExpiryTime - nowInMillis;

		return TimeUnit.MILLISECONDS.toSeconds(relativeExpiryTimeInMillis);
	}

	@Override
	public boolean equals(Object o) {
		return EqualsBuilder.reflectionEquals(this, o);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}

package com.gu.r2.common.cache.memcached;

import java.net.SocketAddress;
import java.util.List;
import java.util.Map;

public interface MemcachedClient {
	Object get(String key);

	void set(String key, Object value, int expiryTimeInSeconds);

	void remove(String key);

	Map<SocketAddress, Map<String, String>> getStats();

	List<SocketAddress> getAvailableServers();

	List<SocketAddress> getUnavailableServers();
}

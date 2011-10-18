package com.gu.cache.memcached;

import java.net.SocketAddress;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public class MemcachedClientStub implements MemcachedClient {

	@Override
	public Object get(String key) {
		return null;
	}

	@Override
	public void set(String key, Object value, int expiryTimeInSeconds) {
	}

	@Override
	public void remove(String key) {
	}

	@Override
	public Map<SocketAddress, Map<String, String>> getStats() {
		return Collections.emptyMap();
	}

	@Override
	public List<SocketAddress> getAvailableServers() {
		return Collections.emptyList();
	}

	@Override
	public List<SocketAddress> getUnavailableServers() {
		return Collections.emptyList();
	}
}

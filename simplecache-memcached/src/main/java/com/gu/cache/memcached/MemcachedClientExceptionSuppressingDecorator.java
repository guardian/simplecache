package com.gu.cache.memcached;


import java.net.SocketAddress;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;


public class MemcachedClientExceptionSuppressingDecorator implements MemcachedClient {

	private static final Logger LOG = Logger.getLogger(MemcachedClientExceptionSuppressingDecorator.class);

	private MemcachedClient memcachedClient;

	public MemcachedClientExceptionSuppressingDecorator(MemcachedClient memcachedClient) {
		this.memcachedClient = memcachedClient;
	}

	public Object get(String key) {
		Object value = null;
		try {
			value = memcachedClient.get(key);
		} catch (RuntimeException e) {
			LOG.trace("Suppressed exception", e);
		}

		return value;
	}

	public void remove(String key) {
		try {
			memcachedClient.remove(key);
		} catch (RuntimeException e) {
			LOG.trace("Suppressed exception", e);
		}
	}

	public void set(String key, Object value, int expiryTimeInSeconds) {
		try {
			memcachedClient.set(key, value, expiryTimeInSeconds);
		} catch (RuntimeException e) {
			LOG.trace("Suppressed exception", e);
		}
	}

	public List<SocketAddress> getAvailableServers() {
		return memcachedClient.getAvailableServers();
	}

	public List<SocketAddress> getUnavailableServers() {
		return memcachedClient.getUnavailableServers();
	}

	public Map<SocketAddress, Map<String, String>> getStats() {
		return memcachedClient.getStats();
	}
}
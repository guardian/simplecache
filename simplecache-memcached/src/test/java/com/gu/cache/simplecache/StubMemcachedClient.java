package com.gu.cache.simplecache;

import com.gu.cache.memcached.MemcachedClient;

import java.net.SocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StubMemcachedClient implements MemcachedClient {

    private final Map<Object, Object> cache = new HashMap<Object, Object>();

    @Override
    public Object get(String key) {
       return cache.get(key);
    }

    @Override
    public void set(String key, Object value, int expiryTimeInSeconds) {
        cache.put(key, value);
    }

    @Override
    public void remove(String key) {
        cache.remove(key);
    }

    @Override
    public Map<SocketAddress, Map<String, String>> getStats() {
        return null;
    }

    @Override
    public List<SocketAddress> getAvailableServers() {
        return null;
    }

    @Override
    public List<SocketAddress> getUnavailableServers() {
        return null;
    }
}
package com.gu.cache.simplecache;

import com.gu.cache.memcached.MemcachedClient;
import org.apache.log4j.Logger;


public class MemcachedSimpleCacheAdaptor extends AbstractSimpleCache {
	private static final Logger LOG = Logger.getLogger(MemcachedSimpleCacheAdaptor.class);
	private final MemcachedClient client;
	private final KeyTranslator keyTranslator;

	public MemcachedSimpleCacheAdaptor(MemcachedClient client, KeyTranslator keyTranslator) {
        this.client = client;
		this.keyTranslator = keyTranslator;
	}

    @Override
    protected Object getDirect(Object key) {
        return client.get(translate(key));
    }

    @Override
    protected void removeDirect(Object key) {
        client.remove(translate(key));
    }

    @Override
    protected void putDirect(Object key, Object cacheValue) {
        client.set(translate(key), cacheValue, 0);
    }

    @Override
    protected void removeAllDirect() {
        LOG.warn("removeAll ignored on memcached");
    }

	private String translate(Object key) {
	    return keyTranslator.translate(key).toString();
	}
}

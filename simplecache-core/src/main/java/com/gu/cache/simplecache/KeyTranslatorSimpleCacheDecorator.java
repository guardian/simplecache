package com.gu.cache.simplecache;

import org.apache.log4j.Logger;

import java.util.concurrent.TimeUnit;

public class KeyTranslatorSimpleCacheDecorator implements SimpleCache {

	private static final Logger LOG = Logger.getLogger(KeyTranslatorSimpleCacheDecorator.class);

	private final SimpleCache delegate;
	private final KeyTranslator keyTranslator;

	public KeyTranslatorSimpleCacheDecorator(SimpleCache delegate, KeyTranslator keyTranslator) {
		this.delegate = delegate;
		this.keyTranslator = keyTranslator;
	}
	
	@Override
	public Object get(Object key) {
        if (LOG.isTraceEnabled()) {
            LOG.trace("get(" + key + ")");
        }

		Object translatedKey = keyTranslator.translate(key);
		return delegate.get(translatedKey);
	}

	@Override
	public CacheValueWithExpiryTime getWithExpiry(Object key) {
        if (LOG.isTraceEnabled()) {
            LOG.trace("getWithExpiry(" + key + ")");
        }

		Object translatedKey = keyTranslator.translate(key);
		return delegate.getWithExpiry(translatedKey);
	}

	@Override
	public void putWithExpiry(Object key, Object value, long lifetime,
			TimeUnit units) {
    	if (LOG.isTraceEnabled()) {
    		LOG.trace(String.format("putWithExpiry(%s) => '%s'", key, value));
    	}

		Object translatedKey = keyTranslator.translate(key);
		delegate.putWithExpiry(translatedKey, value, lifetime, units);
	}

	@Override
	public void remove(Object key) {
        if (LOG.isTraceEnabled()) {
            LOG.trace(String.format("remove(%s)", key) );
        }

		Object translatedKey = keyTranslator.translate(key);
		delegate.remove(translatedKey);
	}

	@Override
	public void removeAll() {
		delegate.removeAll();
	}

    @Override
    public boolean isServeStaleEnabled() {
        return delegate.isServeStaleEnabled();
    }

    @Override
    public void setServeStaleEnabled(boolean serveStaleEnabled) {
        delegate.setServeStaleEnabled(serveStaleEnabled);
    }
}
package com.gu.cache.simplecache;

import java.util.concurrent.TimeUnit;

public class KeyTranslatorSimpleCacheDecorator implements SimpleCache {

	
	private final SimpleCache delegate;
	private final KeyTranslator keyTranslator;

	public KeyTranslatorSimpleCacheDecorator(SimpleCache delegate, KeyTranslator keyTranslator) {
		this.delegate = delegate;
		this.keyTranslator = keyTranslator;
		
	}
	
	@Override
	public Object get(Object key) {
		Object translatedKey = keyTranslator.translate(key);
		return delegate.get(translatedKey);
	}

	@Override
	public CacheValueWithExpiryTime getWithExpiry(Object key) {
		Object translatedKey = keyTranslator.translate(key);
		return delegate.getWithExpiry(translatedKey);
	}

	@Override
	public void putWithExpiry(Object key, Object value, long lifetime,
			TimeUnit units) {
		Object translatedKey = keyTranslator.translate(key);
		delegate.putWithExpiry(translatedKey, value, lifetime, units);
	}

	@Override
	public void remove(Object key) {
		Object translatedKey = keyTranslator.translate(key);
		delegate.remove(translatedKey);
	}

	@Override
	public void removeAll() {
		delegate.removeAll();
	}

}

package com.gu.cache.simplecache;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class KeyTranslatorSimpleCacheDecoratorTest {	
	
	@Mock private SimpleCache simpleCache;
	@Mock private KeyTranslator keyTranslator;
	private KeyTranslatorSimpleCacheDecorator simpleCacheKeyFlatteningDecorator;
	
	@Before public void setUp() {
		MockitoAnnotations.initMocks(this);
		simpleCacheKeyFlatteningDecorator = new KeyTranslatorSimpleCacheDecorator(simpleCache, keyTranslator);
		when(keyTranslator.translate("key")).thenReturn("translated key");		
	}

	@Test public void shouldTranslateKeyAndDelegateGet() {
		simpleCacheKeyFlatteningDecorator.get("key");
		verify(simpleCache).get("translated key");
	}
	
	@Test public void shouldTranslateKeyAndDelegatePutWithExpiryToSimpleCacheDelegate() {
		simpleCacheKeyFlatteningDecorator.putWithExpiry("key", "value", 0, TimeUnit.SECONDS);
		verify(simpleCache).putWithExpiry("translated key", "value", 0, TimeUnit.SECONDS);
	}
	
	@Test public void shouldTranslateKeyAndDelegateGetWithExpiry() {
		simpleCacheKeyFlatteningDecorator.getWithExpiry("key");
		verify(simpleCache).getWithExpiry("translated key");
	}
	
	@Test public void shouldTranslateKeyAndDelegateRemove() {
		simpleCacheKeyFlatteningDecorator.remove("key");
		verify(simpleCache).remove("translated key");
	}
	
}

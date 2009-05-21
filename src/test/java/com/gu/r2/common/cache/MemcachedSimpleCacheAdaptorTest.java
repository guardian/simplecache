package com.gu.r2.common.cache;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.gu.r2.common.cache.memcached.MemcachedClient;

public class MemcachedSimpleCacheAdaptorTest {
	@Mock private MemcachedClient memcachedClient;
	@Mock private KeyTranslator keyTranslator;

	private MemcachedSimpleCacheAdaptor adaptor;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		adaptor = new MemcachedSimpleCacheAdaptor(memcachedClient, keyTranslator);
	}

	@Test
	public void shouldPutToMemcachedUsingKeyTranslator() throws Exception {
		when(keyTranslator.translate("some key")).thenReturn("translated key");

		adaptor.putWithExpiry("some key", "value", 1, TimeUnit.SECONDS);

		verify(memcachedClient).set(eq("translated key"), argThat(hasProperty("value", is("value"))), eq(1));
	}

	@Test
	public void putWithoutExpriryShouldUseADefaultExpiryTime() throws Exception {
		when(keyTranslator.translate("some key")).thenReturn("translated key");

		adaptor.put("some key", "value");

		verify(memcachedClient).set(eq("translated key"), argThat(hasProperty("value", is("value"))), eq((int)TimeUnit.DAYS.toSeconds(1)));

	}

	@Test
	public void shouldReturnNullCorrectlyFromGet() {
		when(keyTranslator.translate("some key")).thenReturn("translated key");
		when(memcachedClient.get("translated key")).thenReturn(null);

		assertThat(adaptor.get("some key"), nullValue());
	}

	@Test
	public void shouldReturnNullCorrectlyFromGetWithExpiry() {
		when(keyTranslator.translate("some key")).thenReturn("translated key");
		when(memcachedClient.get("translated key")).thenReturn(null);

		assertThat(adaptor.getWithExpiry("some key"), nullValue());
	}

}

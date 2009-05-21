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
	private MemcachedSimpleCacheAdaptor adaptor;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		adaptor = new MemcachedSimpleCacheAdaptor(memcachedClient);
	}

	@Test
	public void shouldPutToMemcachedUsingKey() throws Exception {
		adaptor.putWithExpiry("some key", "value", 1, TimeUnit.SECONDS);

		verify(memcachedClient).set(eq("some key"), argThat(hasProperty("value", is("value"))), eq(1));
	}

	@Test
	public void putWithoutExpriryShouldUseADefaultExpiryTime() throws Exception {
		adaptor.put("some key", "value");

		verify(memcachedClient).set(eq("some key"), argThat(hasProperty("value", is("value"))), eq((int)TimeUnit.DAYS.toSeconds(1)));

	}

	@Test
	public void shouldReturnNullCorrectlyFromGet() {
		when(memcachedClient.get("some key")).thenReturn(null);

		assertThat(adaptor.get("some key"), nullValue());
	}

	@Test
	public void shouldReturnNullCorrectlyFromGetWithExpiry() {
		when(memcachedClient.get("some key")).thenReturn(null);

		assertThat(adaptor.getWithExpiry("some key"), nullValue());
	}
}

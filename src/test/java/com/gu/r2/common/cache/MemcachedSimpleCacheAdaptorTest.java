package com.gu.r2.common.cache;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;

import com.gu.r2.common.cache.memcached.MemcachedClient;

public class MemcachedSimpleCacheAdaptorTest {
	@MockitoAnnotations.Mock
	private MemcachedClient memcachedClient;


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
		stub(memcachedClient.get("some key")).toReturn(null);

		assertThat(adaptor.get("some key"), nullValue());
	}

	@Test
	public void shouldReturnNullCorrectlyFromGetWithExpiry() {
		stub(memcachedClient.get("some key")).toReturn(null);

		assertThat(adaptor.getWithExpiry("some key"), nullValue());
	}
}

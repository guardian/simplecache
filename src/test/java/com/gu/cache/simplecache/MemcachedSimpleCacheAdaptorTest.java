package com.gu.cache.simplecache;

import com.gu.cache.memcached.MemcachedClient;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.TimeUnit;

public class MemcachedSimpleCacheAdaptorTest {
	@Mock private MemcachedClient memcachedClient;
	@Mock private KeyTranslator keyTranslator;
    @Mock CacheValueWithExpiryTimeFactory cacheValueWithExpiryTimeFactory;

	private MemcachedSimpleCacheAdaptor adaptor;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		adaptor = new MemcachedSimpleCacheAdaptor(memcachedClient, keyTranslator);
        when(keyTranslator.translate("some key")).thenReturn("translated key");
	}

	@Test
	public void shouldPutToMemcachedUsingKeyTranslator() throws Exception {
		adaptor.putWithExpiry("some key", "value", 1, TimeUnit.SECONDS);

		verify(memcachedClient).set(eq("translated key"), argThat(hasProperty("value", is("value"))), eq(1));
	}

    @Test
    public void shouldSetExpiryToSpecifiedValueIfServeStaleNotEnabled() throws Exception {
        adaptor.putWithExpiry("some key", "value", 10, TimeUnit.SECONDS);
        verify(memcachedClient).set(eq("translated key"), argThat(hasProperty("value", is("value"))), eq(10));
    }

    @Test
    public void shouldSetExpiryToZeroIfServeStaleEnabled() throws Exception {
        adaptor.setServeStaleEnabled(true);
        adaptor.putWithExpiry("some key", "value", 10, TimeUnit.SECONDS);
        verify(memcachedClient).set(eq("translated key"), argThat(hasProperty("value", is("value"))), eq(0));
    }

	@Test
	public void shouldReturnNullCorrectlyFromGet() {
		when(memcachedClient.get("translated key")).thenReturn(null);

		assertThat(adaptor.get("some key"), nullValue());
	}

	@Test
	public void shouldReturnNullCorrectlyFromGetWithExpiry() {
		when(memcachedClient.get("translated key")).thenReturn(null);

		assertThat(adaptor.getWithExpiry("some key"), nullValue());
	}
	
	@Test
	public void shouldNotRemoveAllFromMemcachedBecauseWeNeverWantToAccidentlyClearMemcached() {
		adaptor.removeAll();
		
		verifyZeroInteractions(memcachedClient);
	}

    @Test
    public void shouldReturnStaleObjectWhenGettingWithExpiryIfServeStaleEnabled() {
        CacheValueWithExpiryTime staleValue = new CacheValueWithExpiryTime("value", System.currentTimeMillis() - 1);
        when(memcachedClient.get("translated key")).thenReturn(staleValue);

        adaptor.setServeStaleEnabled(true);
        CacheValueWithExpiryTime returnedValue = adaptor.getWithExpiry("some key");

        assertThat(returnedValue, is(staleValue));
        assertThat(returnedValue.isExpired(), is(true));
    }
    
    @Test
    public void shouldNotReturnStaleObjectWhenGettingEvenIfServeStaleEnabled() {
        CacheValueWithExpiryTime staleValue = new CacheValueWithExpiryTime("value", System.currentTimeMillis() - 1);
        when(memcachedClient.get("translated key")).thenReturn(staleValue);

        adaptor.setServeStaleEnabled(true);
        String returnedValue = (String) adaptor.get("some key");

        assertThat(returnedValue, nullValue());
    }

}
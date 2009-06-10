package com.gu.cache.simplecache;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.when;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnit44Runner;

@RunWith(MockitoJUnit44Runner.class)
public class SoftReferenceSimpleCacheTest {
    private SoftReferenceSimpleCache cache = new SoftReferenceSimpleCache();
	
    @Mock
    private CacheValueWithExpiryTimeFactory mockFactory;

    @Test
    public void shouldPutGetAndRemove() throws Exception {
        assertThat(cache.get("key"), is(nullValue()));
        cache.putWithExpiry("key", "value", 1, TimeUnit.DAYS);
        assertThat(cache.get("key"), is((Object)"value"));
        cache.remove("key");
        assertThat(cache.get("key"), is(nullValue()));
    }
    
    @Test
    public void shouldGetCacheWithExpiryTime() throws Exception {
    	CacheValueWithExpiryTime expectedValue = new CacheValueWithExpiryTime("value", Long.MAX_VALUE);
		when(mockFactory.create("value", 1, TimeUnit.DAYS)).thenReturn(expectedValue);

    	cache.setCacheValueWithExpiryFactory(mockFactory);
    	cache.putWithExpiry("key", "value", 1, TimeUnit.DAYS);
    	
    	CacheValueWithExpiryTime actualValue = cache.getWithExpiry("key");
    	
    	assertThat(actualValue, is(expectedValue));
    }
    
    @Test
    public void shouldReturnNullIfTheRequestedCacheEntiryHasExpired() {
    	CacheValueWithExpiryTime expectedValue = new CacheValueWithExpiryTime("value", System.currentTimeMillis() - 500);
		when(mockFactory.create("value", 1, TimeUnit.DAYS)).thenReturn(expectedValue);

    	cache.setCacheValueWithExpiryFactory(mockFactory);
    	cache.putWithExpiry("key", "value", 1, TimeUnit.DAYS);
    	
    	assertThat(cache.getWithExpiry("key"), is(nullValue()));
    	assertThat(cache.get("key"), is(nullValue()));
    }

}

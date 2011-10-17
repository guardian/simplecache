package com.gu.cache.simplecache;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.concurrent.TimeUnit;

@RunWith(MockitoJUnitRunner.class)
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

    @Test
    public void shouldRemoveAllFromSoftReferenceCache() throws Exception {
    	cache.putWithExpiry("key", "value", 1, TimeUnit.DAYS);
    	cache.putWithExpiry("another key", "another value", 1, TimeUnit.DAYS);
    	
    	cache.removeAll();
    	
    	assertThat(cache.get("key"), is(nullValue()));
    	assertThat(cache.get("another key"), is(nullValue()));
    }

    @Test
    public void shouldCountGetsAndRemoved() throws Exception {
        assertThat(cache.getStatistics().getNumEntries(), is(0L));
        assertThat(cache.getStatistics().getNumHits(), is(0L));
        assertThat(cache.getStatistics().getNumMisses(), is(0L));

        cache.get("key");

        assertThat(cache.getStatistics().getNumEntries(), is(0L));
        assertThat(cache.getStatistics().getNumHits(), is(0L));
        assertThat(cache.getStatistics().getNumMisses(), is(1L));

        cache.putWithExpiry("key", "value", 1, TimeUnit.DAYS);
        cache.get("key");
        cache.get("key");

        assertThat(cache.getStatistics().getNumEntries(), is(1L));
        assertThat(cache.getStatistics().getNumHits(), is(2L));
        assertThat(cache.getStatistics().getNumMisses(), is(1L));
    }

}

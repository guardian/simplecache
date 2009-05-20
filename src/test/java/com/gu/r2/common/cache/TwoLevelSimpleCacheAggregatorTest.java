package com.gu.r2.common.cache;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class TwoLevelSimpleCacheAggregatorTest {
    @MockitoAnnotations.Mock
    private SimpleCache firstLevelCache;
    @MockitoAnnotations.Mock
    private SimpleCache secondLevelCache;
    private TwoLevelSimpleCacheAggregator cache;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        cache = new TwoLevelSimpleCacheAggregator(firstLevelCache, secondLevelCache);
    }

    @Test
    public void getsSatisfiedByFirstLevelCacheShouldNotInvokeSecondLevel() throws Exception {
        stub(firstLevelCache.getWithExpiry("key")).toReturn(new CacheValueWithExpiryTime("value", 0L));

        assertThat(cache.get("key"), is( (Object) "value"));
        
        verifyZeroInteractions(secondLevelCache);
    }

    @Test
    public void getsUnsatisfiedByFirstLevelShouldAskSecondLevelAndPopulate() throws Exception {
	    CacheValueWithExpiryTime valueObject = mock(CacheValueWithExpiryTime.class);
	    stub(valueObject.getInstantaneousSecondsToExpiryTime()).toReturn(5L);
	    stub(valueObject.getValue()).toReturn("value");

	    stub(firstLevelCache.getWithExpiry("key")).toReturn(null);
        stub(secondLevelCache.getWithExpiry("key")).toReturn(valueObject);

        assertThat(cache.get("key"), is( (Object) "value"));

        verify(firstLevelCache).putWithExpiry("key", "value", 5, TimeUnit.SECONDS);
    }

    @Test
    public void getsUnsatisfiedByBothShouldJustReturnNullAndNotPut() throws Exception {
        stub(firstLevelCache.getWithExpiry("key")).toReturn(null);
        stub(secondLevelCache.getWithExpiry("key")).toReturn(null);

        assertThat(cache.get("key"), is(nullValue()));

        verify(firstLevelCache, never()).put("key", null);

    }

    @Test
    public void putsShouldPutToBothLevelCaches() throws Exception {
        cache.put("key", "value");

        verify(firstLevelCache).put("key", "value");
        verify(secondLevelCache).put("key", "value");
    }

    @Test
    public void putWithExprityShouldPutToBothLevelCaches() throws Exception {
        cache.putWithExpiry("key", "value", 5, TimeUnit.HOURS);

        verify(firstLevelCache).putWithExpiry("key", "value", 5, TimeUnit.HOURS);
        verify(secondLevelCache).putWithExpiry("key", "value", 5, TimeUnit.HOURS);
    }

    @Test
    public void removeShouldRemoveFromBothCaches() throws Exception {
        cache.remove("key");

        verify(firstLevelCache).remove("key");
        verify(secondLevelCache).remove("key");
    }

    @Test
    public void shouldMissAndNotPromoteFromSecondLevelCacheWithZeroExpiryTime() {
    	// Cause you may make it eternal in the first level cache.
	    CacheValueWithExpiryTime valueObject = mock(CacheValueWithExpiryTime.class);
	    stub(valueObject.getInstantaneousSecondsToExpiryTime()).toReturn(0L);
	    stub(valueObject.getValue()).toReturn("value");

	    stub(firstLevelCache.getWithExpiry("key")).toReturn(null);
        stub(secondLevelCache.getWithExpiry("key")).toReturn(valueObject);

        assertThat(cache.get("key"), nullValue());

        verify(firstLevelCache, never()).putWithExpiry(anyString(), anyString(), anyInt(), argThat(is(TimeUnit.SECONDS)));
    }

    @Test
    public void shouldMissAndNotPromoteFromSecondLevelCacheWithNegativeExpiryTime() {
    	CacheValueWithExpiryTime valueObject = mock(CacheValueWithExpiryTime.class);
    	stub(valueObject.getInstantaneousSecondsToExpiryTime()).toReturn(-1L);
    	stub(valueObject.getValue()).toReturn("value");

    	stub(firstLevelCache.getWithExpiry("key")).toReturn(null);
    	stub(secondLevelCache.getWithExpiry("key")).toReturn(valueObject);

    	assertThat(cache.get("key"), nullValue());

    	verify(firstLevelCache, never()).putWithExpiry(anyString(), anyString(), anyInt(), argThat(is(TimeUnit.SECONDS)));
    }

}

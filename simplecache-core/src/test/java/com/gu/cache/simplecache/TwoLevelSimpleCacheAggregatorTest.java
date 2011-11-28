package com.gu.cache.simplecache;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.gu.cache.simplecache.CacheValueWithExpiryTime;
import com.gu.cache.simplecache.SimpleCache;
import com.gu.cache.simplecache.TwoLevelSimpleCacheAggregator;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TwoLevelSimpleCacheAggregatorTest {
    @Mock private SoftReferenceSimpleCache firstLevelCache;
    @Mock private SoftReferenceSimpleCache secondLevelCache;
    private TwoLevelSimpleCacheAggregator cache;

    @Before
    public void setUp() throws Exception {
        cache = new TwoLevelSimpleCacheAggregator(firstLevelCache, secondLevelCache);
    }

    @Test
    public void getsSatisfiedByFirstLevelCacheShouldNotInvokeSecondLevel() throws Exception {
        when(firstLevelCache.getWithExpiry("key")).thenReturn(new CacheValueWithExpiryTime("value", 0L));

        assertThat(cache.get("key"), is( (Object) "value"));
        
        verifyZeroInteractions(secondLevelCache);
    }

    @Test
    public void getsUnsatisfiedByFirstLevelShouldAskSecondLevelAndPopulate() throws Exception {
	    CacheValueWithExpiryTime valueObject = mock(CacheValueWithExpiryTime.class);
	    when(valueObject.getInstantaneousSecondsToExpiryTime()).thenReturn(5L);
	    when(valueObject.getValue()).thenReturn("value");

	    when(firstLevelCache.getWithExpiry("key")).thenReturn(null);
	    when(secondLevelCache.getWithExpiry("key")).thenReturn(valueObject);

        assertThat(cache.get("key"), is( (Object) "value"));

        verify(firstLevelCache).putWithExpiry("key", "value", 5, TimeUnit.SECONDS);
    }

    @Test
    public void getsUnsatisfiedByBothShouldJustReturnNullAndNotPut() throws Exception {
    	when(firstLevelCache.getWithExpiry("key")).thenReturn(null);
    	when(secondLevelCache.getWithExpiry("key")).thenReturn(null);

        assertThat(cache.get("key"), nullValue());

        verify(firstLevelCache, never()).putWithExpiry("key", null, 1, TimeUnit.DAYS);
    }

    @Test
    public void putWithExpiryShouldPutToBothLevelCaches() throws Exception {
        cache.putWithExpiry("key", "value", 5, TimeUnit.HOURS);

        verify(firstLevelCache).putWithExpiry("key", "value", 5, TimeUnit.HOURS);
        verify(secondLevelCache).putWithExpiry("key", "value", 5, TimeUnit.HOURS);
    }

    @Test
    public void removeShouldRemoveFromBothCaches() throws Exception {
        cache.remove("key");

        // the second level cache is cleared first, to avoid promotion
        // of items from the second level to a newly clear first level
        
        InOrder inOrder = inOrder(firstLevelCache, secondLevelCache);
        
        inOrder.verify(secondLevelCache).remove("key");
        inOrder.verify(firstLevelCache).remove("key");
    }

    @Test
    public void removeShouldRemoveAllFromBothCaches() throws Exception {
        cache.removeAll();

        InOrder inOrder = inOrder(firstLevelCache, secondLevelCache);
        
        inOrder.verify(secondLevelCache).removeAll();
        inOrder.verify(firstLevelCache).removeAll();
    }
    
    @Test
    public void shouldMissAndNotPromoteFromSecondLevelCacheWithZeroExpiryTime() {
    	// Because in certain implementation you may make it eternal in the first level cache.
	    CacheValueWithExpiryTime valueObject = mock(CacheValueWithExpiryTime.class);
	    when(valueObject.getInstantaneousSecondsToExpiryTime()).thenReturn(0L);
	    when(valueObject.getValue()).thenReturn("value");

	    when(firstLevelCache.getWithExpiry("key")).thenReturn(null);
	    when(secondLevelCache.getWithExpiry("key")).thenReturn(valueObject);

        assertThat(cache.get("key"), nullValue());

        verify(firstLevelCache, never()).putWithExpiry(anyString(), anyString(), anyInt(), argThat(is(TimeUnit.SECONDS)));
    }

    @Test
    public void shouldMissAndNotPromoteFromSecondLevelCacheWithNegativeExpiryTime() {
    	CacheValueWithExpiryTime valueObject = mock(CacheValueWithExpiryTime.class);
    	when(valueObject.getInstantaneousSecondsToExpiryTime()).thenReturn(-1L);
    	when(valueObject.getValue()).thenReturn("value");

    	when(firstLevelCache.getWithExpiry("key")).thenReturn(null);
    	when(secondLevelCache.getWithExpiry("key")).thenReturn(valueObject);

    	assertThat(cache.get("key"), nullValue());

    	verify(firstLevelCache, never()).putWithExpiry(anyString(), anyString(), anyInt(), argThat(is(TimeUnit.SECONDS)));
    }

    @Test
    public void shouldEnableServeStaleStateOnBothCaches() {
        cache.setServeStaleEnabled(true);

    	verify(firstLevelCache).setServeStaleEnabled(true);
    	verify(secondLevelCache).setServeStaleEnabled(true);
    }

    @Test
    public void shouldDisableServeStaleStateOnBothCaches() {
        cache.setServeStaleEnabled(false);

    	verify(firstLevelCache).setServeStaleEnabled(false);
    	verify(secondLevelCache).setServeStaleEnabled(false);
    }

    @Test
    public void shouldNotRemoveIfServeStaleEnabled() {
        cache.setServeStaleEnabled(true);

        cache.remove("value");

    	verify(firstLevelCache, never()).remove(anyObject());
    	verify(secondLevelCache, never()).remove(anyObject());
    }

    @Test
    public void shouldNotRemoveAllIfServeStaleEnabled() {
        cache.setServeStaleEnabled(true);

        cache.removeAll();

    	verify(firstLevelCache, never()).removeAll();
    	verify(secondLevelCache, never()).removeAll();
    }

    @Test
    public void shouldNotDecaySecondLevelCacheValuesInServeStaleMode() {
        cache.setServeStaleEnabled(true);
        cache.setMinTtlForPromotion(5);

        CacheValueWithExpiryTime valueObject = mock(CacheValueWithExpiryTime.class);
	    when(valueObject.getInstantaneousSecondsToExpiryTime()).thenReturn(-600L);
	    when(valueObject.getValue()).thenReturn("value");

	    when(firstLevelCache.getWithExpiry("key")).thenReturn(null);
	    when(secondLevelCache.getWithExpiry("key")).thenReturn(valueObject);

        assertThat(cache.get("key"), is(valueObject.getValue()));
    }

    @Test
    public void shouldPromoteDecayedSecondLevelCacheValuesInServeStaleMode() {
        cache.setServeStaleEnabled(true);
        cache.setMinTtlForPromotion(5);

        CacheValueWithExpiryTime valueObject = mock(CacheValueWithExpiryTime.class);
	    when(valueObject.getInstantaneousSecondsToExpiryTime()).thenReturn(-600L);
	    when(valueObject.getValue()).thenReturn("value");

	    when(firstLevelCache.getWithExpiry("key")).thenReturn(null);
	    when(secondLevelCache.getWithExpiry("key")).thenReturn(valueObject);

        cache.get("key");

        verify(firstLevelCache).putWithExpiry("key", "value", -600, TimeUnit.SECONDS);
    }

}

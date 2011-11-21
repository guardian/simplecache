package com.gu.cache.simplecache;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

@RunWith(MockitoJUnitRunner.class)
public class SoftReferenceSimpleCacheTest {
    private SoftReferenceSimpleCache cache = new SoftReferenceSimpleCache();

    @Before
    public void setUp() {
        Clock.unfreeze();
    }

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
        Clock.freeze();

        cache.putWithExpiry("key", "value", 1, TimeUnit.DAYS);

        CacheValueWithExpiryTime actualValue = cache.getWithExpiry("key");

        assertThat(actualValue.getValue(), is((Object)"value"));
        assertThat(actualValue.getInstantaneousSecondsToExpiryTime(), is(TimeUnit.DAYS.toSeconds(1)));
    }
    
    @Test
    public void shouldReturnNullIfTheRequestedCacheEntiryHasExpired() {
    	cache.putWithExpiry("key", "value", 1, TimeUnit.MINUTES);

        Clock.freeze(Clock.currentTimeMillis() + TimeUnit.HOURS.toMillis(1));

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

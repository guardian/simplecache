package com.gu.cache.simplecache;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class EhcacheSimpleCacheAdaptorTest {
    private Ehcache ehcache;
    private EhcacheSimpleCacheAdaptor cache;

    @Before
    public void setUp() throws Exception {
        CacheManager manager = new CacheManager();

        if (!manager.cacheExists("testCache")) {
            manager.addCache("testCache");
        }

        ehcache = manager.getCache("testCache");
        cache = new EhcacheSimpleCacheAdaptor(ehcache);

        cache.setServeStaleEnabled(false);
        Clock.unfreeze();
    }

    @Test
    public void shouldPutGetAndRemove() throws Exception {
        assertThat(cache.get("key"), nullValue());

        cache.putWithExpiry("key", "value", 1, TimeUnit.DAYS);
        assertThat(cache.get("key"), is((Object)"value"));

        cache.remove("key");
        assertThat(cache.get("key"), nullValue());
    }

    @Test
    public void shouldPutGetAndRemoveToEhcache() throws Exception {
        assertThat(cache.get("key"), nullValue());

        cache.putWithExpiry("key", "value", 1, TimeUnit.DAYS);
        CacheValueWithExpiryTime cached = (CacheValueWithExpiryTime) ehcache.get("key").getObjectValue();
        assertThat(cached.getValue(), is((Object) "value"));

        cache.remove("key");
        assertThat(ehcache.get("key"), nullValue());

        Clock.freeze();
        cache.putWithExpiry("key2", "value", 1000, TimeUnit.MILLISECONDS);
        assertThat(ehcache.get("key2").getTimeToLive(), is(0));
        cached = (CacheValueWithExpiryTime) ehcache.get("key2").getObjectValue();
        assertThat(cached.getInstantaneousSecondsToExpiryTime(), is(1L));
    }

    @Test
    public void shouldGetStaleIfEnabled() throws Exception {
        cache.setServeStaleEnabled(true);
        Clock.freeze();

        cache.putWithExpiry("key", "value", 1, TimeUnit.MINUTES);

        // Make the entry stale
        Clock.freeze(Clock.currentTimeMillis() + TimeUnit.HOURS.toMillis(1));

        assertThat(cache.get("key"), is((Object)"value"));

        CacheValueWithExpiryTime actualValue = cache.getWithExpiry("key");
        assertThat(actualValue.getValue(), is((Object)"value"));
        long expectStaleTime = TimeUnit.HOURS.toSeconds(1) - TimeUnit.MINUTES.toSeconds(1);
        assertThat(actualValue.getInstantaneousSecondsSinceExpiryTime(), is(expectStaleTime));
    }

    @Test
    public void shouldNotGetStaleIfDisabled() throws Exception {
        cache.setServeStaleEnabled(false);
        Clock.freeze();

        cache.putWithExpiry("key", "value", 1, TimeUnit.MINUTES);

        // Make the entry stale
        Clock.freeze(Clock.currentTimeMillis() + TimeUnit.HOURS.toMillis(1));

        assertThat(cache.get("key"), nullValue());
        CacheValueWithExpiryTime actualValue = cache.getWithExpiry("key");
        assertThat(actualValue, nullValue());
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

    	assertThat(cache.getWithExpiry("key"), nullValue());
    	assertThat(cache.get("key"), nullValue());
    }

    @Test
    public void shouldRemove() throws Exception {
    	cache.putWithExpiry("key", "value", 1, TimeUnit.DAYS);
    	cache.putWithExpiry("another key", "another value", 1, TimeUnit.DAYS);

    	cache.remove("key");

    	assertThat(cache.get("key"), nullValue());
    	assertThat(cache.get("another key"), notNullValue());
    }

    @Test
    public void shouldNotRemoveInServeStaleMode() throws Exception {
        cache.setServeStaleEnabled(true);
    	cache.putWithExpiry("key", "value", 1, TimeUnit.DAYS);
    	cache.putWithExpiry("another key", "another value", 1, TimeUnit.DAYS);

    	cache.remove("key");

    	assertThat(cache.get("key"), notNullValue());
    	assertThat(cache.get("another key"), notNullValue());
    }

    @Test
    public void shouldRemoveAll() throws Exception {
    	cache.putWithExpiry("key", "value", 1, TimeUnit.DAYS);
    	cache.putWithExpiry("another key", "another value", 1, TimeUnit.DAYS);

    	cache.removeAll();

    	assertThat(cache.get("key"), nullValue());
    	assertThat(cache.get("another key"), nullValue());
    }

    @Test
    public void shouldRemoveAllFromEhcache() throws Exception {
    	cache.putWithExpiry("key", "value", 1, TimeUnit.DAYS);
    	cache.putWithExpiry("another key", "another value", 1, TimeUnit.DAYS);
    	
    	cache.removeAll();
    	
    	assertThat(ehcache.getSize(), is(0));
    }

    @Test
    public void shouldNotRemoveAllInServeStaleMode() throws Exception {
        cache.setServeStaleEnabled(true);
    	cache.putWithExpiry("key", "value", 1, TimeUnit.DAYS);
    	cache.putWithExpiry("another key", "another value", 1, TimeUnit.DAYS);

    	cache.removeAll();

    	assertThat(cache.get("key"), notNullValue());
    	assertThat(cache.get("another key"), notNullValue());
    }

    @Test
	public void shouldReturnNullCorrectlyFromGet() {
		assertThat(cache.get("key"), nullValue());
		assertThat(cache.getWithExpiry("key"), nullValue());
	}

    @Test
    public void shouldPutAndGetEvenWhenTheValuesAreNotSerlizable() throws Exception {
        assertThat(cache.get("key"), nullValue());

        Object nonSerializable = new Object();
        cache.putWithExpiry("key", nonSerializable, 1, TimeUnit.DAYS);
        assertThat(cache.get("key"), sameInstance(nonSerializable));
        
        cache.remove("key");
        assertThat(cache.get("key"), nullValue());
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

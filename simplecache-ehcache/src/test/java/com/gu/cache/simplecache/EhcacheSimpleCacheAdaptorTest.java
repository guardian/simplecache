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
    private EhcacheSimpleCacheAdaptor adaptor;

    @Before
    public void setUp() throws Exception {
        CacheManager manager = new CacheManager();

        if (!manager.cacheExists("testCache")) {
            manager.addCache("testCache");
        }

        ehcache = manager.getCache("testCache");
        adaptor = new EhcacheSimpleCacheAdaptor(ehcache);
    }

    @Test
    public void shouldPutGetAndRemoveToEhcache() throws Exception {
        assertThat(adaptor.get("key"), is(nullValue()));
        adaptor.putWithExpiry("key", "value", 1, TimeUnit.DAYS);
        assertThat(adaptor.get("key"), is((Object)"value"));
        adaptor.remove("key");
        assertThat(adaptor.get("key"), is(nullValue()));

        adaptor.putWithExpiry("key2", "value", 1000, TimeUnit.MILLISECONDS);
        assertThat(ehcache.get("key2").getTimeToLive(), is(1));
    }
    
    @Test
    public void shouldRemoveAllFromEhcache() throws Exception {
    	adaptor.putWithExpiry("key", "value", 1, TimeUnit.DAYS);
    	adaptor.putWithExpiry("another key", "another value", 1, TimeUnit.DAYS);
    	
    	adaptor.removeAll();
    	
    	assertThat(ehcache.getSize(), is(0));
    }

    @Test
	public void shouldReturnNullCorrectlyFromGet() {
		assertThat(adaptor.get("key"), is(nullValue()));
		assertThat(adaptor.getWithExpiry("key"), is(nullValue()));
	}

    @Test
    public void shouldPutAndGetEvenWhenTheValuesAreNotSerlizable() throws Exception {
        assertThat(adaptor.get("key"), is(nullValue()));

        Object nonSerializable = new Object();
        adaptor.putWithExpiry("key", nonSerializable, 1, TimeUnit.DAYS);
        assertThat(adaptor.get("key"), sameInstance(nonSerializable));
        
        adaptor.remove("key");
        assertThat(adaptor.get("key"), is(nullValue()));
    }
    
    @Test
    public void shouldCountGetsAndRemoved() throws Exception {
        assertThat(adaptor.getStatistics().getNumEntries(), is(0L));
        assertThat(adaptor.getStatistics().getNumHits(), is(0L));
        assertThat(adaptor.getStatistics().getNumMisses(), is(0L));

        adaptor.get("key");

        assertThat(adaptor.getStatistics().getNumEntries(), is(0L));
        assertThat(adaptor.getStatistics().getNumHits(), is(0L));
        assertThat(adaptor.getStatistics().getNumMisses(), is(1L));

        adaptor.putWithExpiry("key", "value", 1, TimeUnit.DAYS);
        adaptor.get("key");
        adaptor.get("key");

        assertThat(adaptor.getStatistics().getNumEntries(), is(1L));
        assertThat(adaptor.getStatistics().getNumHits(), is(2L));
        assertThat(adaptor.getStatistics().getNumMisses(), is(1L));
    }

}

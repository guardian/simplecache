package com.gu.cache.simplecache;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import org.junit.Test;
import org.junit.Before;

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
	public void shouldReturnNullCorrectlyFromGet() {
		assertThat(adaptor.get("key"), is(nullValue()));
		assertThat(adaptor.getWithExpiry("key"), is(nullValue()));
	}


    @Test
    public void shouldPutAndGetEvenWhenTheValuesAreNotSerlizable() throws Exception {
        assertThat(adaptor.get("key"), is(nullValue()));
        NonSerializableClass myObject = new NonSerializableClass();
        adaptor.putWithExpiry("key", myObject, 1, TimeUnit.DAYS);
        assertThat(adaptor.get("key"), sameInstance((Object)myObject));
        adaptor.remove("key");
        assertThat(adaptor.get("key"), is(nullValue()));

    }

    private class NonSerializableClass {
    }
}

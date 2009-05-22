package com.gu.cache.simplecache;

import java.util.concurrent.TimeUnit;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import org.junit.Test;

import com.gu.cache.simplecache.EhcacheSimpleCacheAdaptor;

public class EhcacheSimpleCacheAdaptorTest {
    @Test
    public void shouldPutGetAndRemoveToEhcache() throws Exception {

        CacheManager manager = new CacheManager();

        if (!manager.cacheExists("testCache")) {
            manager.addCache("testCache");
        }

        Ehcache ehcache = manager.getCache("testCache");

        EhcacheSimpleCacheAdaptor adaptor = new EhcacheSimpleCacheAdaptor(ehcache);
        
        assertThat(adaptor.get("key"), is(nullValue()));
        adaptor.put("key", "value");
        assertThat(adaptor.get("key"), is((Object)"value"));
        adaptor.remove("key");
        assertThat(adaptor.get("key"), is(nullValue()));

        adaptor.putWithExpiry("key2", "value", 1000, TimeUnit.MILLISECONDS);
        assertThat(ehcache.get("key2").getTimeToLive(), is(1));
    }

	@Test
	public void shouldReturnNullCorrectlyFromGet() {
		CacheManager manager = new CacheManager();

		if (!manager.cacheExists("testCache")) {
		    manager.addCache("testCache");
		}

		Ehcache ehcache = manager.getCache("testCache");

		EhcacheSimpleCacheAdaptor adaptor = new EhcacheSimpleCacheAdaptor(ehcache);

		assertThat(adaptor.get("key"), is(nullValue()));
		assertThat(adaptor.getWithExpiry("key"), is(nullValue()));
	}

}

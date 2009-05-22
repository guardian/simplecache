package com.gu.cache.simplecache.hibernate;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

import com.gu.cache.simplecache.SimpleCache;

public class SimpleCacheQueryResultsRegionTest {

		@Test
	public void shoudlInsertIntoCacheWithTheLifetimeSpecifiedInConstructor() throws Exception {
		SimpleCache cache = mock(SimpleCache.class);
		SimpleCacheQueryResultsRegion queryResultsRegion
				= new SimpleCacheQueryResultsRegion ("name", null, cache, 99);


		queryResultsRegion.put("key", "value");

		Mockito.verify(cache).putWithExpiry("key", "value", 99, TimeUnit.SECONDS);
	}
}

package com.gu.cache.simplecache.hibernate;

import java.util.concurrent.TimeUnit;

import org.hibernate.cache.access.AccessType;
import org.hibernate.cache.access.CollectionRegionAccessStrategy;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

import com.gu.cache.simplecache.SimpleCache;

public class SimpleCacheCollectionRegionTest {
	@Test
	public void shoudlInsertIntoCacheWithTheLifetimeSpecifiedInConstructor() throws Exception {
		SimpleCache cache = mock(SimpleCache.class);
		SimpleCacheCollectionRegion simpleCacheRegion
				= new SimpleCacheCollectionRegion("name", null, cache, 25);

		CollectionRegionAccessStrategy accessStrategy = simpleCacheRegion.buildAccessStrategy(AccessType.READ_ONLY);

		accessStrategy.putFromLoad("key", "value", 0, null, false);

		Mockito.verify(cache).putWithExpiry("key", "value", 25, TimeUnit.SECONDS);
	}

}

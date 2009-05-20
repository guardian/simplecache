package com.gu.r2.common.cache.hibernate;

import java.util.concurrent.TimeUnit;

import org.hibernate.cache.access.AccessType;
import org.hibernate.cache.access.EntityRegionAccessStrategy;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

import com.gu.r2.common.cache.SimpleCache;

public class SimpleCacheEntityRegionTest {
	@Test
	public void shoudlInsertIntoCacheWithTheLifetimeSpecifiedInConstructor() throws Exception {
		SimpleCache cache = mock(SimpleCache.class);
		SimpleCacheEntityRegion simpleCacheEntityRegion
				= new SimpleCacheEntityRegion("name", null, cache, 25);

		EntityRegionAccessStrategy accessStrategy = simpleCacheEntityRegion.buildAccessStrategy(AccessType.READ_ONLY);
		
		accessStrategy.putFromLoad("key", "value", 0, null, false);

		Mockito.verify(cache).putWithExpiry("key", "value", 25, TimeUnit.SECONDS);
	}

}

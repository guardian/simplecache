package com.gu.cache.simplecache;

import com.google.common.collect.MapMaker;
import com.gu.cache.simplecache.statistics.Statistics;
import com.gu.cache.simplecache.statistics.StatisticsProvider;

import java.util.concurrent.ConcurrentMap;

public class SoftReferenceSimpleCache extends AbstractSimpleCache implements StatisticsProvider {

    private final ConcurrentMap<Object, Object> cache;

    public SoftReferenceSimpleCache() {
        cache = new MapMaker()
                .concurrencyLevel(50)
                .softValues()
                .makeMap();
    }

    protected Object getDirect(Object key) {
       return cache.get(key);
    }

    protected void removeDirect(Object key) {
        cache.remove(key);
    }

    protected void putDirect(Object key, Object cacheValue) {
        cache.put(key, cacheValue);
    }

    protected void removeAllDirect() {
        cache.clear();
    }

    @Override
    public Statistics getStatistics() {
        return getStatisticsCounter().asStatistics(cache.size());
    }
}

package com.gu.cache.simplecache;

import com.gu.cache.simplecache.statistics.Statistics;
import com.gu.cache.simplecache.statistics.StatisticsProvider;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

public class EhcacheSimpleCacheAdaptor extends AbstractSimpleCache implements StatisticsProvider {
    private final Ehcache cache;

    public EhcacheSimpleCacheAdaptor(Ehcache cache) {
        this.cache = cache;
    }

    @Override
    protected Object getDirect(Object key) {
        Element element = cache.get(key);
        if (element == null) {
            return null;
        }

        return element.getObjectValue();
    }

    @Override
    protected void removeDirect(Object key) {
        cache.remove(key);
    }

    @Override
    protected void putDirect(Object key, Object cacheValue) {
        Element element = new Element(key, cacheValue);
        element.setTimeToLive(0);
        cache.put(element);
    }

    @Override
    protected void removeAllDirect() {
        cache.removeAll();
    }

    @Override
    public Statistics getStatistics() {
        return getStatisticsCounter().asStatistics(cache.getSize());
    }
}

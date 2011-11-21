package com.gu.cache.simplecache;

public class NullSimpleCache extends AbstractSimpleCache {

    @Override
    protected Object getDirect(Object key) {
        return null;
    }

    @Override
    protected void removeDirect(Object key) { }

    @Override
    protected void putDirect(Object key, Object cacheValue) { }

    @Override
    protected void removeAllDirect() { }
}

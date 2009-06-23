package com.gu.cache.simplecache.statistics;

import java.util.concurrent.atomic.AtomicLong;

public class StatisticsCounter {
    private AtomicLong numHits = new AtomicLong();
    private AtomicLong numMisses = new AtomicLong();

    public void hit() {
        numHits.getAndIncrement();
    }

    public void miss() {
        numMisses.getAndIncrement();
    }

    public Statistics asStatistics(int numEntries) {
        return new Statistics(numEntries, numHits.get(), numMisses.get());
    }
}

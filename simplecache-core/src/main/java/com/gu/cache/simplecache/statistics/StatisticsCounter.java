package com.gu.cache.simplecache.statistics;

import java.util.concurrent.atomic.AtomicLong;

public class StatisticsCounter {
    private AtomicLong numHits = new AtomicLong();
    private AtomicLong numMisses = new AtomicLong();
    private AtomicLong numWrites = new AtomicLong();
    private AtomicLong numRemoves = new AtomicLong();
    private AtomicLong numRemoveAlls = new AtomicLong();
    private AtomicLong numServeStales = new AtomicLong();

    public void hit() {
        numHits.getAndIncrement();
    }

    public void miss() {
        numMisses.getAndIncrement();
    }

    public void write() {
        numWrites.getAndIncrement();
    }

    public void remove() {
        numRemoves.getAndIncrement();
    }

    public void removeAll() {
        numRemoveAlls.getAndIncrement();
    }

    public void serveStale() {
        numServeStales.getAndIncrement();
    }

    public Statistics asStatistics(int numEntries) {
        return new Statistics(numEntries,
                numHits.get(),
                numMisses.get(),
                numWrites.get(),
                numRemoves.get(),
                numRemoveAlls.get(),
                numServeStales.get());
    }
}

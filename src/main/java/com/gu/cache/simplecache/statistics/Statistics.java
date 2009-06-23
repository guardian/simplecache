package com.gu.cache.simplecache.statistics;

public class Statistics {
    private final long numEntries;
    private final long numHits;
    private final long numMisses;

    public Statistics(long numEntries, long numHits, long numMisses) {
        this.numEntries = numEntries;
        this.numHits = numHits;
        this.numMisses = numMisses;
    }

    public long getNumEntries() {
        return numEntries;
    }

    public long getNumHits() {
        return numHits;
    }

    public long getNumMisses() {
        return numMisses;
    }

    public double getHitRatio() {
        return numHits / (double) (numHits + numMisses);
    }
}

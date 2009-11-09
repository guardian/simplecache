package com.gu.cache.management;


public class MemcachedServer {
    private final String hostName;
    private final int port;
    private final boolean available;

    public MemcachedServer(String hostName, int port, boolean available) {
        this.hostName = hostName;
        this.port = port;
        this.available = available;
    }

    public String getHostName() {
        return hostName;
    }

    public int getPort() {
        return port;
    }

    public boolean isAvailable() {
        return available;
    }
}


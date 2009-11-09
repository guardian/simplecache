package com.gu.cache.management;

import com.gu.cache.memcached.MemcachedClient;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.List;

public class MemcachedStatisticsService {
    private final MemcachedClient memcachedClient;

    public MemcachedStatisticsService(MemcachedClient memcachedClient) {
        this.memcachedClient = memcachedClient;
    }

    public MemcachedStatistics getCacheStatistics() {
        MemcachedStatistics result = new MemcachedStatistics();

        addServers(result, memcachedClient.getAvailableServers(), true);
        addServers(result, memcachedClient.getUnavailableServers(), false);

        return result;
    }

    private void addServers(MemcachedStatistics result, List<SocketAddress> servers, boolean isAvailable) {
        for (SocketAddress socketAddress : servers) {
            if (socketAddress instanceof InetSocketAddress) {
                InetSocketAddress inetSocketAddress = (InetSocketAddress) socketAddress;
                result.addServer(new MemcachedServer(inetSocketAddress.getHostName(), inetSocketAddress.getPort(), isAvailable));
            }
        }
    }
}

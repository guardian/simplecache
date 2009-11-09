package com.gu.cache.management;

import java.util.ArrayList;
import java.util.List;

public class MemcachedStatistics {
    private List<MemcachedServer> servers = new ArrayList<MemcachedServer>();

    public List<MemcachedServer> getServers() {
        return servers;
    }

    public void addServer(MemcachedServer server) {
        servers.add(server);
    }
}

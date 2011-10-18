package com.gu.cache.memcached;

import com.gu.management.timing.TimingMetric;
import net.spy.memcached.MemcachedClientIF;
import net.spy.memcached.MemcachedNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class SpyMemcachedClientAdaptor implements MemcachedClient {
	private static final Logger LOG = LoggerFactory.getLogger(SpyMemcachedClientAdaptor.class);

	private final MemcachedClientIF client;
	private final TimingMetric metric;

	public SpyMemcachedClientAdaptor(MemcachedClientIF client, TimingMetric metric) {
		this.client = client;
		this.metric = metric;
	}

	@Override
	public Object get(final String key) {
		try {
			final MemcachedLoggingStopWatch stopWatch = new MemcachedLoggingStopWatch(LOG, "get " + key, metric);

			return stopWatch.executeAndLog(new Callable<Object>() {
				@Override
				public Object call() throws Exception {
					final Object value = client.get(key);
					stopWatch.operationOutcome(value == null ? "MISS" : "HIT");
					return value;
				}
			});
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void set(final String key, final Object value, final int expiryTimeInSeconds) {
		try {
			new MemcachedLoggingStopWatch(LOG, "set " + key, metric).executeAndLog(new Callable<Object>() {
				@Override
				public Object call() throws Exception {
					client.set(key, expiryTimeInSeconds, value);
                    return null;
				}
			});
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void remove(final String key) {
		try {
			new MemcachedLoggingStopWatch(LOG, "remove " + key, metric).executeAndLog(new Callable<Object>() {
				@Override
				public Object call() throws Exception {
					client.delete(key);
                    return null;
				}
			});
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Map<SocketAddress, Map<String, String>> getStats() {
		LOG.info("Supplementary Memcached stats");
		Collection<MemcachedNode> nodes = client.getNodeLocator().getAll();
		for (MemcachedNode node : nodes) {
			LOG.info(node.toString());
		}

		return client.getStats();
	}

	@Override
	public List<SocketAddress> getAvailableServers() {
		return new ArrayList<SocketAddress>(client.getAvailableServers());
	}

	@Override
	public List<SocketAddress> getUnavailableServers() {
		return new ArrayList<SocketAddress>(client.getUnavailableServers());
	}
}

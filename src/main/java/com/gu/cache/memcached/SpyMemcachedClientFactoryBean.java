package com.gu.cache.memcached;

import com.gu.management.timing.TimingMetric;
import net.spy.memcached.ConnectionFactory;
import net.spy.memcached.MemcachedClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.AbstractFactoryBean;

import java.net.InetSocketAddress;
import java.util.List;


public class SpyMemcachedClientFactoryBean extends AbstractFactoryBean {

	private static final Logger LOG = Logger.getLogger(SpyMemcachedClientFactoryBean.class);

	private final List<InetSocketAddress> addresses;

	private final ConnectionFactory connectionFactory;
	private final TimingMetric metric;

	public SpyMemcachedClientFactoryBean(List<InetSocketAddress> addresses, ConnectionFactory connectionFactory, TimingMetric metric) {
		this.addresses = addresses;
		this.connectionFactory = connectionFactory;
		this.metric = metric;
	}

	@Override
	protected Object createInstance() throws Exception {
		if (addresses.isEmpty()) {
			LOG.warn("We have been configured with no memcached servers. Using stub memcached client.");
			return new MemcachedClientStub();
		}

		System.setProperty("net.spy.log.LoggerImpl", "net.spy.memcached.compat.log.Log4JLogger");
		LOG.info("Configured memcached servers: " + addresses);

		return new SpyMemcachedClientAdaptor(new MemcachedClient(connectionFactory, addresses), metric);

	}

	@Override
	public Class<com.gu.cache.memcached.MemcachedClient> getObjectType() {
		return com.gu.cache.memcached.MemcachedClient.class;
	}
}

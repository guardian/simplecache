package com.gu.cache.memcached;

import net.spy.memcached.ConnectionFactory;
import net.spy.memcached.DefaultConnectionFactory;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpyMemcachedClientFactoryBeanTest {
    ConnectionFactory connectionFactory;

    @Before
    public void setup() {
        connectionFactory = new DefaultConnectionFactory(1,1);
    }
    @Test
    public void testThatFactoryReturnsObjectOfCorrectTypeWithEmptyList() throws Exception {
        final SpyMemcachedClientFactoryBean factoryBean = new SpyMemcachedClientFactoryBean(new ArrayList<InetSocketAddress>(), connectionFactory, null);
        final Object instance = factoryBean.createInstance();
        final Class<?> clientClass = factoryBean.getObjectType();
        assertThat(instance, instanceOf(clientClass));
    }
    @Test
    public void testThatFactoryReturnsObjectOfCorrectTypeWithNonEmptyList() throws Exception {
        final List<InetSocketAddress> arrayList = Arrays.asList(new InetSocketAddress("localhost", 80));
        final SpyMemcachedClientFactoryBean factoryBean = new SpyMemcachedClientFactoryBean(arrayList, connectionFactory, null);
        final Object instance = factoryBean.createInstance();
        final Class<?> clientClass = factoryBean.getObjectType();
        assertThat(instance, instanceOf(clientClass));
    }
}
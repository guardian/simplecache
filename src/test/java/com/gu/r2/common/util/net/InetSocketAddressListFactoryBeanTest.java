package com.gu.r2.common.util.net;

import static com.gu.testsupport.matchers.Matchers.collectionContainingOnly;
import static org.hamcrest.MatcherAssert.assertThat;

import java.net.InetSocketAddress;
import java.util.List;

import org.junit.Test;

public class InetSocketAddressListFactoryBeanTest {

	@Test
	public void shouldCreateAddressesListFromString() throws Exception {
		String addresses = "depression.int.gnl:999, despair.int.gnl:666, inferiority.int.gnl:8888";

		List<InetSocketAddress> list = getAddressesFromFactory(addresses);

		assertThat(list, collectionContainingOnly(new InetSocketAddress("depression.int.gnl", 999),
				new InetSocketAddress("despair.int.gnl", 666),
				new InetSocketAddress("inferiority.int.gnl", 8888)));
	}

	@SuppressWarnings("unchecked")
	private List<InetSocketAddress> getAddressesFromFactory(String addresses) throws Exception {
		InetSocketAddressListFactoryBean factory = new InetSocketAddressListFactoryBean(addresses);
		factory.afterPropertiesSet();

		List<InetSocketAddress> list = (List<InetSocketAddress>) factory.getObject();
		return list;
	}
}

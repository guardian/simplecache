package com.gu.util.net;

import static java.lang.Integer.parseInt;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.config.AbstractFactoryBean;


public class InetSocketAddressListFactoryBean extends AbstractFactoryBean {

	private final static Pattern ADDRESS_PATTERN = Pattern.compile("([\\w.\\-]+):(\\d+)");

	private final String inetSocketAddresses;

	public InetSocketAddressListFactoryBean(String addresses) {
		this.inetSocketAddresses = addresses;
	}

	@Override
	protected Object createInstance() throws Exception {
		List<InetSocketAddress> inetSocketAddressList = new LinkedList<InetSocketAddress>();

		Matcher matcher = ADDRESS_PATTERN.matcher(inetSocketAddresses);
		while (matcher.find()) {
			String addr = matcher.group(1);
			int port = parseInt(matcher.group(2));
			inetSocketAddressList.add(new InetSocketAddress(addr,port));
		}

        sortAddresses(inetSocketAddressList);

        return inetSocketAddressList;
	}

    private void sortAddresses(List<InetSocketAddress> inetSocketAddressList) {
        Collections.sort(inetSocketAddressList, new Comparator<InetSocketAddress>() {
            @Override
            public int compare(InetSocketAddress o1, InetSocketAddress o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });
    }

    @SuppressWarnings("unchecked")
	@Override
	public Class getObjectType() {
		return List.class;
	}
}

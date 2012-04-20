package com.gu.cache.memcached;

import com.gu.cache.simplecache.KeyTranslator;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MemcachedKeyTranslator implements KeyTranslator {
	private final String version;

	@Autowired
	public MemcachedKeyTranslator(String version) {
		this.version = version;
	}

	public String translate(Object key) {
		try {
			return String.format("%s:%s", version, URLEncoder.encode(String.valueOf(key),"US-ASCII"));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}

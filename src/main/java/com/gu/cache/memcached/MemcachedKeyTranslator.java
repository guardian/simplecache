package com.gu.cache.memcached;

import com.gu.cache.simplecache.KeyTranslator;
import com.gu.management.manifest.Manifest;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MemcachedKeyTranslator implements KeyTranslator {
	private final Manifest manifest;

	@Autowired
	public MemcachedKeyTranslator(Manifest manifest) {
		this.manifest = manifest;
	}

	public String translate(Object key) {
		try {
			return String.format("%s:%s", manifest.getRevisionNumber(), URLEncoder.encode(String.valueOf(key),"US-ASCII"));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}

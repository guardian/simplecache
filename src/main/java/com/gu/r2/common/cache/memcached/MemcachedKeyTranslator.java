package com.gu.r2.common.cache.memcached;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.gu.manifest.Manifest;
import com.gu.r2.common.cache.KeyTranslator;

public class MemcachedKeyTranslator implements KeyTranslator {
	private final Manifest manifest;

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

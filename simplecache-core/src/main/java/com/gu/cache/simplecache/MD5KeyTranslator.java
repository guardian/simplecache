package com.gu.cache.simplecache;


public class MD5KeyTranslator implements KeyTranslator {
	private MD5HashGenerator md5Generator;

	public MD5KeyTranslator( MD5HashGenerator md5Generator) {
		this.md5Generator = md5Generator;		
	}
	
	public String translate(Object key) {
			return md5Generator.hash(key);
	}
}

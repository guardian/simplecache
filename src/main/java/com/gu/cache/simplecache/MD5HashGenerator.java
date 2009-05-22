package com.gu.cache.simplecache;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

public class MD5HashGenerator {
	
	public String hash(Object objectToHash) {
		MessageDigest algorithm = getMessageDigestAlgorithm();
		algorithm.update(objectToHash.toString().getBytes());

		return new String(Hex.encodeHex(algorithm.digest()));
	}

	private MessageDigest getMessageDigestAlgorithm() {
		try {
			return MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
}

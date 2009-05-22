package com.gu.cache.memcached;

import net.spy.memcached.KetamaConnectionFactory;
import net.spy.memcached.transcoders.SerializingTranscoder;
import net.spy.memcached.transcoders.Transcoder;


public class ConfigurableKetamaConnectionFactory extends KetamaConnectionFactory {

	private final long operationTimeout;
	private final int compressionThresholdKB;

	public ConfigurableKetamaConnectionFactory(long operationTimeout, int compressionThresholdKB) {
		this.operationTimeout = operationTimeout;
		this.compressionThresholdKB = compressionThresholdKB;
	}

	@Override
	public long getOperationTimeout() {
		return operationTimeout;
	}

	@Override
	public Transcoder<Object> getDefaultTranscoder() {
		SerializingTranscoder transcoder = new SerializingTranscoder();
		transcoder.setCompressionThreshold(compressionThresholdKB * 1024);

		return new SerializingTranscoder();
	}
}

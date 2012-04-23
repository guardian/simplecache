package com.gu.cache.memcached;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class MemcachedKeyTranslatorTest {

	private MemcachedKeyTranslator translator = new MemcachedKeyTranslator("123");

	@Test
	public void shouldAddManifestRevisionNumberToKeysWithSimpleCharacters() throws Exception {
		assertThat(translator.translate("key"), is("123:key"));
	}

	@Test
	public void shouldPerformToStringOnNonStringObjects() throws Exception {
		assertThat(translator.translate(new StringBuilder("key")), is("123:key"));
	}

	@Test
	public void shouldPerformATransformationToRemoveNonMemcachedSupportedCharacters() throws Exception {
		assertThat(translator.translate(new StringBuilder("key with spaces")), is("123:key+with+spaces"));
	}

}

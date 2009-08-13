package com.gu.cache.memcached;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.gu.cache.memcached.MemcachedKeyTranslator;
import com.gu.manifest.components.Manifest;

public class MemcachedKeyTranslatorTest {
	private MemcachedKeyTranslator translator;

	@Mock
	private Manifest manifestMock;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		translator = new MemcachedKeyTranslator(manifestMock);

		when(manifestMock.getRevisionNumber()).thenReturn(123L);
	}

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

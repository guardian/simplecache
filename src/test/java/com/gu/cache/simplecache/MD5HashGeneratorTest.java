package com.gu.cache.simplecache;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;


public class MD5HashGeneratorTest {

	@Test
	public void testHashHexEncodeCorrectlyEncodesAllBytes() {

		String hash = new MD5HashGenerator().hash("SELECT * FROM content_draft");
		assertThat(hash.length(), equalTo(32));
		assertThat(hash, equalTo("00bbcf6d2fe803dc4c70e1b53fb867a6"));
	}

}

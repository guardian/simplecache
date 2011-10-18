package com.gu.cache.memcached;

import com.gu.management.timing.TimingMetric;
import org.slf4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.concurrent.Callable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;


public class MemcachedLoggingStopWatchTest {

	private @Mock Logger log;
	private @Mock
    TimingMetric metric;

	@Before
	public void setUp() {
		initMocks(this);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldExecutePassedCallableAndReturnResult() throws Exception {
		MemcachedLoggingStopWatch memcachedLoggingStopWatch = new MemcachedLoggingStopWatch(log, null, metric);
		Callable<String> callable = mock(Callable.class);
		when(callable.call()).thenReturn("fish");

		String actualResult = memcachedLoggingStopWatch.executeAndLog(callable);

		assertThat(actualResult, equalTo("fish"));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldLogActivityAndOperationOutcome() throws Exception {
		when(log.isTraceEnabled()).thenReturn(true);
		final MemcachedLoggingStopWatch memcachedLoggingStopWatch = new MemcachedLoggingStopWatch(log, "myActivity", metric);

		memcachedLoggingStopWatch.executeAndLog(new Callable<Object>() {
			public Object call() {
				memcachedLoggingStopWatch.operationOutcome("Good outcome");
				return null;
			}
		});

		verify(log).trace(argThat(allOf(containsString("myActivity"),containsString("Good outcome"))));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldLogException() throws Exception {
		final MemcachedLoggingStopWatch memcachedLoggingStopWatch = new MemcachedLoggingStopWatch(log, "myActivity", metric);

		Callable callable = mock(Callable.class);

		final IllegalArgumentException iae = new IllegalArgumentException();
		when(callable.call()).thenThrow(iae);

		try {
			memcachedLoggingStopWatch.executeAndLog(callable);
			fail("Exception should be re-thrown");
		} catch(IllegalArgumentException e) {}

		verify(log).warn(argThat(allOf(containsString("myActivity"),containsString("threw exception"))));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldUpdateTimingMetricWithActivityDuration() throws Exception {
		MemcachedLoggingStopWatch memcachedLoggingStopWatch = new MemcachedLoggingStopWatch(log, null, metric);
		Callable<String> callable = mock(Callable.class);
		when(callable.call()).thenReturn("fish");

		memcachedLoggingStopWatch.executeAndLog(callable);

		verify(metric).recordTimeSpent(anyLong());
	}

}
package com.gu.cache.memcached;

import com.gu.management.timing.TimingMetric;
import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;

import java.util.concurrent.Callable;


public class MemcachedLoggingStopWatch {

	private String outcome;
	private final Logger log;
	private final StopWatch stopWatch = new StopWatch();
	private final String activity;
	private final TimingMetric metric;

	public MemcachedLoggingStopWatch(Logger log, String activity, TimingMetric metric) {
		this.log = log;
		this.activity = activity;
		this.metric = metric;
	}

	public <T> T executeAndLog(Callable<T> callable) throws Exception {
		T result;
		stopWatch.start();
		try {
			result = callable.call();
		} catch (Exception e) {
			stopWatch.stop();
			log.warn(activity + " threw exception after " + stopWatch.getTime() + " ms: "+e.getMessage());
			if (log.isTraceEnabled()) {
				log.trace(activity+" full stack trace follows",e);
			}
			throw e;
		}
		stopWatch.stop();
		metric.recordTimeSpent(stopWatch.getTime());

		if (log.isTraceEnabled()) {
			String outcomeString = outcome!=null ? " Outcome: " + outcome : "";
			log.trace(activity + " completed in " + stopWatch.getTime() + " ms" + outcomeString);
		}
		return result;
	}

	public void operationOutcome(String outcome) {
		this.outcome = outcome;
	}

}

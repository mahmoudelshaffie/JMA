package com.optile.jma.execution;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.optile.jma.api.IJob;
import com.optile.jma.execution.api.IJobScheduler;

public class FixedRateRepeatableJobScheduler implements IJobScheduler {
	
	private ScheduledExecutorService scheduledExecutorService;

	public FixedRateRepeatableJobScheduler(IJob job, long startTimeout, long delayTimeout, TimeUnit unit) {
		scheduledExecutorService = Executors.newScheduledThreadPool(1);
		scheduledExecutorService.schedule(new JobRunner(job), startTimeout, unit);
	}

	@Override
	public void terminate() {
		if (!scheduledExecutorService.isTerminated()) {
			scheduledExecutorService.shutdownNow();
		}
	}

}

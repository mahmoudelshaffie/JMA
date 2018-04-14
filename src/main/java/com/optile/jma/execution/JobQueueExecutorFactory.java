package com.optile.jma.execution;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.optile.jma.config.apis.IJobQueueConfig;

public class JobQueueExecutorFactory {

	public static ExecutorService createExecutor(IJobQueueConfig config) {
		if (config.allowsParllelRuns()) {
			int noOfThreads = config.getMaxConcurrentJobs();
			if (noOfThreads > 0) {
				return Executors.newFixedThreadPool(noOfThreads);
			} else {
				return Executors.newCachedThreadPool();
			}
		} else {
			return Executors.newSingleThreadExecutor();
		}
	}
}

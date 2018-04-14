package com.optile.jma.config.apis;

public interface IJobQueueConfig {
	boolean allowsParllelRuns();
	String getName();
	int getCapacity();
	int getMaxConcurrentJobs();
	boolean isTerminable();
}

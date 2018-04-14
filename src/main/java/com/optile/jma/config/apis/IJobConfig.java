package com.optile.jma.config.apis;

public interface IJobConfig {
	int getPriority();
	String getJobId();
	long getTaskTimeout();
	long getTaskRetryDelay();
	int getMaxAttemptsPerTask();
	IScheduleConfig getScheduleConfig();
	long getJobTimeout();
	boolean allowsConcurrentTasks();
	boolean isScheduled();
}

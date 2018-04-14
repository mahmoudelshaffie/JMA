package com.optile.jma.config;

import com.optile.jma.config.apis.IJobConfig;
import com.optile.jma.config.apis.IScheduleConfig;

public class JobConfig implements IJobConfig {
	private int priority;
	private String jobId;
	private long taskTimeout = 0;
	private long taskRetryDelay = 0;
	private int maxAttemptsPerTask = 0;
	private IScheduleConfig scheduleConfig;
	private long jobTimeout = 0;
	private boolean concurrentTasks = false;

	public JobConfig(int priority, String jobId, long taskTimeout, long taskRetryDelay, int maxAttemptsPerTask,
			IScheduleConfig scheduleConfig, long jobTimeout, boolean concurrentTasks) {
		this.priority = priority;
		this.jobId = jobId;
		this.taskTimeout = taskTimeout;
		this.taskRetryDelay = taskRetryDelay;
		this.maxAttemptsPerTask = maxAttemptsPerTask;
		this.scheduleConfig = scheduleConfig;
		this.jobTimeout = jobTimeout;
		this.concurrentTasks = concurrentTasks;
	}

	public int getPriority() {
		return priority;
	}

	public String getJobId() {
		return jobId;
	}

	public long getTaskTimeout() {
		return taskTimeout;
	}

	public long getTaskRetryDelay() {
		return taskRetryDelay;
	}

	public int getMaxAttemptsPerTask() {
		return maxAttemptsPerTask;
	}

	public IScheduleConfig getScheduleConfig() {
		return scheduleConfig;
	}

	public long getJobTimeout() {
		return jobTimeout;
	}

	public boolean allowsConcurrentTasks() {
		return this.concurrentTasks;
	}

	@Override
	public boolean isScheduled() {
		return scheduleConfig != null ? true : false;
	}
}

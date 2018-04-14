package com.optile.jma.config;

import com.optile.jma.config.apis.IJobConfig;
import com.optile.jma.config.apis.IScheduleConfig;

public class JobConfigBuilder {

	private int priority = 0;
	private String jobId;
	private long taskTimeout = 0;
	private long taskRetryDelay = 0;
	private int maxAttemptsPerTask = 0;
	private IScheduleConfig scheduleConfig;
	private long jobTimeout = 0;
	private boolean concurrentTasks = false;

	public JobConfigBuilder withTimeoutPerTask(long timeout) {
		this.taskTimeout = timeout;
		return this;
	}

	public JobConfigBuilder withJobID(String id) {
		this.jobId = id;
		return this;
	}

	public JobConfigBuilder schedule(IScheduleConfig scheduleConfig) {
		this.scheduleConfig = scheduleConfig;
		return this;
	}

	public JobConfigBuilder withJobPriority(int priority) {
		this.priority = priority;
		return this;
	}

	public JobConfigBuilder withJobTimeout(long jobTimeout) {
		this.jobTimeout = jobTimeout;
		return this;
	}

	public JobConfigBuilder withMaxAttemptsPerTask(int maxAttempts) {
		this.maxAttemptsPerTask = maxAttempts;
		return this;
	}

	public JobConfigBuilder withTaskRetryDelay(long taskRetryDelay) {
		this.taskRetryDelay = taskRetryDelay;
		return this;
	}
	
	public JobConfigBuilder concurrentTasks(boolean concurrentTasks) {
		this.concurrentTasks = concurrentTasks;
		return this;
	}

	public IJobConfig build() {
		return new JobConfig(priority, jobId, taskTimeout, taskRetryDelay, maxAttemptsPerTask,
				scheduleConfig, jobTimeout, concurrentTasks);
	}
}

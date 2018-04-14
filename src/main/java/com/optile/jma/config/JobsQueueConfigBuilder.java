package com.optile.jma.config;

import com.optile.jma.config.apis.IJobQueueConfig;

public class JobsQueueConfigBuilder {
	
	private String name;
	private boolean parllelRuns = false;
	private int maxConcurrent = 10;
	private int capacity = 10;
	private boolean terminable = true;

	public JobsQueueConfigBuilder parllelRuns(boolean parllelRuns) {
		this.parllelRuns = parllelRuns;
		return this;
	}
	
	public JobsQueueConfigBuilder setName(String name) {
		this.name = name;
		return this;
	}
	
	public JobsQueueConfigBuilder setMaxConcurrentJobs(int maxConcurrent) {
		this.maxConcurrent = maxConcurrent;
		return this;
	}
	
	public JobsQueueConfigBuilder terminable(boolean terminable) {
		this.terminable = terminable;
		return this;
	}
	
	public JobsQueueConfigBuilder setCapacity(int capacity) {
		this.capacity = capacity;
		return this;
	}
	
	public IJobQueueConfig build() {
		return new JobsQueueConfig(name, parllelRuns, maxConcurrent, capacity, terminable);
	}
}

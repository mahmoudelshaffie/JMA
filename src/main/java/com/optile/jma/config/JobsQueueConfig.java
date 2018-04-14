package com.optile.jma.config;

import com.optile.jma.config.apis.IJobQueueConfig;

public class JobsQueueConfig implements IJobQueueConfig {

	private String name;
	private boolean parllelRuns;
	private int maxConcurrent;
	private int capacity;
	private boolean terminable;

	public JobsQueueConfig(String name, boolean parllelRuns, int maxConcurrent, int capacity, boolean terminable) {
		this.name = name;
		this.parllelRuns = parllelRuns;
		this.maxConcurrent = maxConcurrent;
		this.capacity = capacity;
		this.terminable = terminable;
	}

	public boolean allowsParllelRuns() {
		return this.parllelRuns;
	}

	public String getName() {
		return this.name;
	}

	public int getCapacity() {
		return this.capacity;
	}

	public int getMaxConcurrentJobs() {
		return this.maxConcurrent;
	}

	public boolean isTerminable() {
		return this.terminable;
	}

}

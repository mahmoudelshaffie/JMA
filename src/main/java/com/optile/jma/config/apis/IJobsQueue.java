package com.optile.jma.config.apis;

import java.util.Iterator;

import com.optile.jma.api.IJob;

public interface IJobsQueue {
	String getName();
	Iterator<IJob> getJobs();
	void enqueue(IJob job);
	void schedule(IJob job, IScheduleConfig scheduleConfig);
	void start();
	void stop();
	boolean terminable();
}

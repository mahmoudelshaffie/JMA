package com.optile.jma;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.optile.jma.config.apis.IJobsQueue;

public class JobManager {

	private volatile Map<String, IJobsQueue> jobQueues;
	private static final JobManager singleton = new JobManager();

	public static JobManager getJobManager() {
		return singleton;
	}
		
	private JobManager() {
		jobQueues = new ConcurrentHashMap<String, IJobsQueue>();
	}
	
	public void start() {
		
	}
	
	public void stop(boolean force) {
		
	}
	
	public IJobsQueue getJobsQueue(String queueId) {
		return this.jobQueues.get(queueId);
	}
	
	public void addJobsQueue(IJobsQueue queue) {
		
	}
	
	public void deleteJobsQueue(String queueId) {
		
	}
	
	public void startJobsQueue(String queueId) {
		
	}
	
	
	public void pauseJobsQueue(String queueId) {
		
	}
	
	public void terminateJobsQueue(String queueId) {
		
	}
}

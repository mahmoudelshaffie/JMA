package com.optile.jma;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;

import com.optile.jma.api.IJob;
import com.optile.jma.config.apis.IJobQueueConfig;
import com.optile.jma.config.apis.IJobsQueue;
import com.optile.jma.config.apis.IScheduleConfig;
import com.optile.jma.excecptions.IsNotTerminableException;
import com.optile.jma.excecptions.OverCapacityException;
import com.optile.jma.execution.JobQueueExecutorFactory;
import com.optile.jma.execution.JobSchedulerFactory;
import com.optile.jma.execution.api.IJobScheduler;

public class JobsQueue implements IJobsQueue {

	private IJobQueueConfig config;
	private ConcurrentSkipListSet<IJob> jobs;
	private volatile boolean running = false;
	private ExecutorService executor;
	private Map<String, IJobScheduler> schedules;

	public JobsQueue(IJobQueueConfig config) {
		this.config = config;
		Comparator<IJob> compartor = new Comparator<IJob>() {
			@Override
			public int compare(IJob j1, IJob j2) {
				if (j1.getPriority() > j2.getPriority()) {
					return 10;
				} else if (j1.getPriority() < j2.getPriority()) {
					return -10;
				} else {
					return 0;
				}
			}
		};

		this.jobs = new ConcurrentSkipListSet<>(compartor);
		
		executor = JobQueueExecutorFactory.createExecutor(config);
		schedules = new ConcurrentHashMap<>();
	}
	
	private void checkIfExceedsCapacity() {
		if (config.getCapacity() > 0 
				&& (jobs.size() + schedules.size() == config.getCapacity())) {
			throw new OverCapacityException();
		}
	}

	@Override
	public String getName() {
		return this.config.getName();
	}

	@Override
	public Iterator<IJob> getJobs() {
		return jobs.iterator();
	}

	@Override
	public void start() {
		synchronized (this) {
			if (running) {
				return;
			} else {
				running = true;
			}
		}

		executor.execute(new Runnable() {
			@Override
			public void run() {
				while (running) {
					while (jobs.size() > 0) {
						IJob job = jobs.pollFirst();
						job.execute();
					}
				}
			}
		});
	}

	@Override
	public void stop() {
		if (terminable() && !executor.isTerminated()) {
			if (running) {
				executor.shutdownNow();
				for (String scheduledJob : schedules.keySet()) {
					IJobScheduler scheduler = schedules.get(scheduledJob);
					scheduler.terminate();
				}
			}
		} else {
			throw new IsNotTerminableException();
		}
	}

	@Override
	public boolean terminable() {
		return this.config.isTerminable();
	}

	@Override
	public synchronized void enqueue(IJob job) {
		checkIfExceedsCapacity();
		this.jobs.add(job);
	}
	
	@Override   
	public synchronized void schedule(IJob job, IScheduleConfig scheduleConfig) {
		checkIfExceedsCapacity();
		
		IJobScheduler scheduler = JobSchedulerFactory.createSchedule(job, scheduleConfig);
		schedules.put(job.getID(), scheduler);
	}
}

package com.optile.jma;

import static com.optile.jma.test.TaskTestUtils.getSleepyTask;
import static com.optile.jma.test.TaskTestUtils.getSuccessTask;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.optile.jma.api.IJob;
import com.optile.jma.api.ITask;
import com.optile.jma.api.JobStatus;
import com.optile.jma.config.JobConfigBuilder;
import com.optile.jma.config.JobsQueueConfigBuilder;
import com.optile.jma.config.ScheduleConfig;
import com.optile.jma.config.apis.IJobConfig;
import com.optile.jma.config.apis.IJobQueueConfig;
import com.optile.jma.config.apis.IJobsQueue;

public class IllustrationTest {

	
	private IJob getSuccessfullNonAsyncJob(String jobId, int priority) {
		ITask task11 = getSuccessTask();
		ITask task12 = getSuccessTask();
		ITask task13 = getSuccessTask();
		IJobConfig j1Config = new JobConfigBuilder().withJobID(jobId).withJobPriority(priority).build();
		IJob job = new Job(j1Config);
		job.addTask(task11);
		job.addTask(task12);
		job.addTask(task13);
		return job;
	}
	
	private IJob getSuccessfullAsyncJob(String jobId, int priority) {
		ITask task11 = getSleepyTask(500);
		ITask task12 = getSuccessTask();
		ITask task13 = getSleepyTask(1500);
		ITask task14 = getSuccessTask();
		IJobConfig j1Config = new JobConfigBuilder().withJobID(jobId).concurrentTasks(true).withMaxAttemptsPerTask(5).withTimeoutPerTask(2000).withJobPriority(priority).build();
		IJob job = new Job(j1Config);
		job.addTask(task11);
		job.addTask(task12);
		job.addTask(task13);
		job.addTask(task14);
		return job;
	}
	
	private IJob getFailureAsync(String jobId, int priority) throws Exception {
		ITask task11 = getSleepyTask(100);
		ITask task12 = getSuccessTask();
		ITask task13 = getSuccessTask();
		ITask task14 = getSleepyTask(250);
		IJobConfig j1Config = new JobConfigBuilder().withJobID(jobId).withMaxAttemptsPerTask(3).withTimeoutPerTask(200).withJobPriority(priority).build();
		IJob job = new Job(j1Config);
		job.addTask(task13);
		job.addTask(task11);
		job.addTask(task12);
		job.addTask(task14);
		return job;
	}
	
	@Test
	public void realTest() throws Exception {
		IJob j1 = getSuccessfullNonAsyncJob("j1", 1000);		
		IJob j2 = getSuccessfullNonAsyncJob("j2", 500);
		IJob j3 = getSuccessfullNonAsyncJob("j3", 100);
		IJob j4 = getSuccessfullNonAsyncJob("j3", 50);
		
		IJob j5 = getSuccessfullAsyncJob("j5", 70);
		IJob j6 = getSuccessfullAsyncJob("j6", 120);
		IJob j7 = getSuccessfullAsyncJob("j7", 10);
		
		IJob j0 = getFailureAsync("j0", 1100);
		IJob j8 = getFailureAsync("j8", 700);
		IJob j9 = getFailureAsync("j9", 80);
		
		
		IJobQueueConfig queueConfig = new JobsQueueConfigBuilder().parllelRuns(true).setMaxConcurrentJobs(3).setName("Queue").terminable(false).build();
		IJobsQueue queue = new JobsQueue(queueConfig);
		
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				queue.start();
				queue.enqueue(j9);
				queue.enqueue(j8);
				queue.enqueue(j7);
				queue.enqueue(j1);
				queue.enqueue(j2);
				queue.schedule(j3, new ScheduleConfig(TimeUnit.MILLISECONDS, 1, 0));
				queue.enqueue(j4);
				queue.enqueue(j5);
				queue.enqueue(j0);
				queue.schedule(j6, new ScheduleConfig(TimeUnit.MILLISECONDS, 1, 0));
			}
		});
				
		t.start();
		Thread.currentThread().sleep(15000);
		

		assertEquals(JobStatus.SUCCESS, j1.getStatus());
		assertEquals(JobStatus.SUCCESS, j2.getStatus());
		assertEquals(JobStatus.SUCCESS, j3.getStatus());
		assertEquals(JobStatus.SUCCESS, j4.getStatus());
		assertEquals(JobStatus.SUCCESS, j5.getStatus());
		assertEquals(JobStatus.SUCCESS, j6.getStatus());
		assertEquals(JobStatus.SUCCESS, j7.getStatus());
		
		assertEquals(JobStatus.FAILED, j0.getStatus());
		assertEquals(JobStatus.FAILED, j8.getStatus());
		assertEquals(JobStatus.FAILED, j9.getStatus());
	}
}


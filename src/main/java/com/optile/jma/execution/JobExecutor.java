package com.optile.jma.execution;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.optile.jma.api.ITask;
import com.optile.jma.config.apis.IJobConfig;
import com.optile.jma.execution.api.IJobExecutor;

public class JobExecutor implements IJobExecutor {
	
	private static final int MAX_CONCCURRENT_TASKS = 5;

	private volatile List<ITask> executedTasks;
	private IJobConfig jobConfig;
	private volatile ExecutorService executor;

	public JobExecutor(IJobConfig jobConfig) {
		executedTasks = new LinkedList<ITask>();
		this.jobConfig = jobConfig;
		
		if (jobConfig.allowsConcurrentTasks()) {
			executor = Executors.newFixedThreadPool(MAX_CONCCURRENT_TASKS);
		} else {
			executor = Executors.newSingleThreadExecutor();
		}
	}

	public Future<ExecutionResult> execute(Iterator<ITask> tasks) {
		Future<ExecutionResult> jobResult = executor.submit(() -> {
			while (tasks.hasNext()) {
				ITask task = tasks.next();

				TaskExecutor taskExecutor = new TaskExecutor(jobConfig.getMaxAttemptsPerTask(),
						jobConfig.getTaskTimeout(), jobConfig.getTaskRetryDelay());
				Future<ExecutionResult> taskPromise = taskExecutor.executeTask(task);
				ExecutionResult taskResult = taskPromise.get(); 
				if (taskResult.isSuccessful()) {
					executedTasks.add(task);
				} else {
					rollback();
					return taskResult;
				}
			}

			return new ExecutionResult();
		});
		
		executor.shutdown();
		return jobResult;
	}


	public void terminate() {
		if (! executor.isTerminated()) {
			executor.shutdownNow();
			this.rollback();
		}

	}
	
	protected void rollback() {
		for (ITask task : executedTasks) {
			task.undo();
		}
	}

}

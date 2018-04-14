package com.optile.jma;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.optile.jma.api.IJob;
import com.optile.jma.api.ITask;
import com.optile.jma.api.JobStatus;
import com.optile.jma.config.apis.IJobConfig;
import com.optile.jma.excecptions.IllegalStateException;
import com.optile.jma.execution.ExecutionResult;
import com.optile.jma.execution.JobExecutor;
import com.optile.jma.execution.api.IJobExecutor;

public class Job implements IJob {

	private IJobConfig config;
	private JobStatus status;
	private ConcurrentLinkedQueue<ITask> tasks;
	private IJobExecutor executor;

	public Job(final IJobConfig config) {
		this.config = config;
		this.updateStatus(JobStatus.QUEUED);
		tasks = new ConcurrentLinkedQueue<ITask>();
		this.executor = new JobExecutor(config);
	}

	public String getID() {
		return this.config.getJobId();
	}

	public int getPriority() {
		return this.config.getPriority();
	}

	public void addTask(ITask task) throws IllegalStateException {
		if (this.status == JobStatus.RUNNING) {
			throw new IllegalStateException(this.status.name());
		}
		this.tasks.offer(task);
	}

	public boolean allowsParllelTasks() {
		return this.config.allowsConcurrentTasks();
	}

	public JobStatus getStatus() {
		return this.status;
	}

	public Iterator<ITask> getTasks() {
		return this.tasks.iterator();
	}

	public void execute() throws IllegalStateException {
		if (this.status == JobStatus.RUNNING) {
			throw new IllegalStateException(this.status.name());
		}

		this.updateStatus(JobStatus.RUNNING);

		Future<ExecutionResult> promise = this.executor.execute(tasks.iterator());
		ExecutionResult jobResult;
		try {
			jobResult = promise.get();
		} catch (InterruptedException e) {
			jobResult = new ExecutionResult(e);
		} catch (ExecutionException e) {
			jobResult = new ExecutionResult(e);
		}

		if (jobResult.isSuccessful()) {
			this.updateStatus(JobStatus.SUCCESS);
		} else {
			this.updateStatus(JobStatus.FAILED);
		}
	}

	public void terminate() {
		if (this.status == JobStatus.RUNNING) {
			this.executor.terminate();
			updateStatus(JobStatus.QUEUED);
		}
	}

	protected synchronized void updateStatus(JobStatus newStatus) {
		this.status = newStatus;
	}

	@Override
	public IJobConfig getConfig() {
		return config;
	}

	@Override
	public boolean equals(Object obj) {
		return ((IJob) obj).getID().equals(config.getJobId());
	}
}

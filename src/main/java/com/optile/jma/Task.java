package com.optile.jma;

import com.optile.jma.api.ICommand;
import com.optile.jma.api.ITask;
import com.optile.jma.api.TaskStatus;
import com.optile.jma.config.apis.ITaskConfig;

public class Task implements ITask {

	private ITaskConfig config;
	private ICommand command;
	private volatile TaskStatus status;
	
	public Task(final ITaskConfig config, final ICommand command) {
		this.config = config;
		this.command = command;
		this.updateStatus(TaskStatus.NEW);
	}
	
	public String getID() {
		return this.config.getId();
	}

	public String getDescription() {
		return this.config.getDescription();
	}

	public void run() throws Exception {
		this.updateStatus(TaskStatus.RUNNING);
		try {
			this.command.execute();
			this.updateStatus(TaskStatus.SUCCESSFULL);
		} catch (Exception e) {
			this.updateStatus(TaskStatus.FAILED);
			throw e;
		}
	}

	public void undo() {
		if (this.status == TaskStatus.SUCCESSFULL) {
			this.command.undo();
		}
		
		this.updateStatus(TaskStatus.CANCELLED);
	}
	
	public TaskStatus getStatus() {
		return this.status;
	}
	
	protected synchronized void updateStatus(TaskStatus newStatus) {
		this.status = newStatus;
	}
}

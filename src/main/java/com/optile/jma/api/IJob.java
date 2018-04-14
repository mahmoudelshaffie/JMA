package com.optile.jma.api;

import java.util.Iterator;

import com.optile.jma.config.apis.IJobConfig;
import com.optile.jma.excecptions.IllegalStateException;

public interface IJob {
	IJobConfig getConfig();
	String getID();
	int getPriority();
	void addTask(ITask task);
	boolean allowsParllelTasks();
	JobStatus getStatus();
	Iterator<ITask> getTasks();
	void execute() throws IllegalStateException;
	void terminate();
}

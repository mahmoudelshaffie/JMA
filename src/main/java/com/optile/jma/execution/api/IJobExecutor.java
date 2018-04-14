package com.optile.jma.execution.api;

import java.util.Iterator;
import java.util.concurrent.Future;

import com.optile.jma.api.ITask;
import com.optile.jma.execution.ExecutionResult;

public interface IJobExecutor {
	Future<ExecutionResult> execute(Iterator<ITask> tasks);
	void terminate();
}

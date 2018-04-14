package com.optile.jma.execution;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.optile.jma.api.ITask;

public class TaskExecutor {

	private final int maxAttempts;
	private final long timeout;
	private final long retryDelay;

	public TaskExecutor(int maxAttempts, long timeout, long retryDelay) {
		this.maxAttempts = maxAttempts;
		this.timeout = timeout;
		this.retryDelay = retryDelay;
	}

	public Future<ExecutionResult> executeTask(ITask task) {
		return CompletableFuture.supplyAsync(() -> {	
			int trails = 1;
			ExecutionResult result;
			do {
				try {
					try {
						if (timeout > 0) {
							result = getTaskCallable(task).get(timeout, TimeUnit.MILLISECONDS);
						} else {
							result = getTaskCallable(task).get();
						}
					} catch (TimeoutException e2) {
						result = new ExecutionResult(e2);
					} catch (ExecutionException e) {
						result = new ExecutionResult(e);
					}

					if (result.isSuccessful() == false) {
						++trails;
						if (retryDelay > 0) {
							Thread.sleep(retryDelay);
						}
					} else {
						break;
					}
				} catch (InterruptedException e2) { // Interruption, Killing Signal
					result = new ExecutionResult(e2);
					// Since It may be interrupted during execution, or it
					// finished
					// the execution and just waiting for timeout
					task.undo();
					break;
				}

			} while (trails <= maxAttempts);

			return result;
		});

	}
	
	private CompletableFuture<ExecutionResult> getTaskCallable(ITask task) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				task.run();
				return new ExecutionResult();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
	}
}

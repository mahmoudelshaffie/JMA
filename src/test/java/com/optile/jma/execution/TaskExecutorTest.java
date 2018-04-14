package com.optile.jma.execution;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.concurrent.Future;

import org.junit.Test;

import com.optile.jma.api.ITask;
import com.optile.jma.api.TaskStatus;

public class TaskExecutorTest {

	private final boolean success = true;
	private final boolean failure = false;
	private final int onlyOneTime = 1;

	@Test
	public void testExecuteTaskWithTaskAlwaysSuccessShouldBeExecutedOnlyOneTimeAndReturnSuccessfulResult()
			throws Exception {
		int noOfAttempts = 5;
		long timeout = 0;
		long retryDelay = 0;
		TaskExecutor taskExecutor = new TaskExecutor(noOfAttempts, timeout, retryDelay);

		ITask task = mock(ITask.class);

		Future<ExecutionResult> future = taskExecutor.executeTask(task);
		ExecutionResult result = future.get();
		assertTrue(result.isSuccessful() == success);
		assertNull("Expected No Error", result.getError());
		verify(task, times(onlyOneTime)).run();
	}

	@Test
	public void testExecuteTaskWithTimeoutAndDelayAndTaskTakesMoreThanTimeoutShouldBeExecutedNoOfAttemptsAndReturnFailureResult()
			throws Exception {
		int noOfAttempts = 5;
		long timeout = 1000;
		long retryDelay = 1000;
		TaskExecutor taskExecutor = new TaskExecutor(noOfAttempts, timeout, retryDelay);

		ITask task = new ITask() {

			@Override
			public void undo() {
				// TODO Auto-generated method stub

			}

			@Override
			public void run() throws Exception {
				Thread.currentThread().sleep(timeout + 200);
			}

			@Override
			public TaskStatus getStatus() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getID() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getDescription() {
				// TODO Auto-generated method stub
				return null;
			}
		};
		ITask spyTask = spy(task);

		Future<ExecutionResult> future = taskExecutor.executeTask(spyTask);
		ExecutionResult result = future.get();
		assertTrue(result.isSuccessful() == failure);
		assertNotNull("Expected Has Error", result.getError());
		verify(spyTask, times(noOfAttempts)).run();
	}

	@Test
	public void testExecuteTaskWithTaskAlwaysFailsShouldBeExecutedNoOfAttemptsAndReturnFailureResult()
			throws Exception {
		int noOfAttempts = 5;
		long timeout = 0;
		long retryDelay = 0;
		TaskExecutor taskExecutor = new TaskExecutor(noOfAttempts, timeout, retryDelay);

		ITask task = mock(ITask.class);
		doThrow(new Exception()).when(task).run();

		Future<ExecutionResult> future = taskExecutor.executeTask(task);
		ExecutionResult result = future.get();
		assertTrue(result.isSuccessful() == failure);
		assertNotNull("Expected Has Error", result.getError());
		verify(task, times(noOfAttempts)).run();
	}

}

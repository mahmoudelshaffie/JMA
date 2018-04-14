package com.optile.jma.execution;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.Test;

import com.optile.jma.api.ITask;
import com.optile.jma.config.JobConfigBuilder;
import com.optile.jma.config.apis.IJobConfig;
import com.optile.jma.test.SleepingTask;

public class JobExecutorTest {

	private final boolean success = true;
	private final boolean failure = false;
	private final int onlyOneTime = 1;

	@Test
	public void testExecuteJobWithSuccessfullTasksShouldBeExecutedSuccessfully() throws Exception {
		IJobConfig jobConfig = new JobConfigBuilder().withJobID("Id2").build();
		JobExecutor executor = new JobExecutor(jobConfig);
		ITask task = mock(ITask.class);
		ITask task1 = mock(ITask.class);
		ITask task2 = mock(ITask.class);
		List<ITask> tasks = new ArrayList<>();
		tasks.add(task);
		tasks.add(task1);
		tasks.add(task2);
		Future<ExecutionResult> result = executor.execute(tasks.iterator());
		assertTrue(result.get().isSuccessful() == success);
		verify(task, times(onlyOneTime)).run();
		verify(task1, times(onlyOneTime)).run();
		verify(task2, times(onlyOneTime)).run();
	}

	@Test
	public void testExecuteJobWithTasksOneOfThemFailShouldBeFailedAndRollbackExectuedTasks() throws Exception {
		IJobConfig jobConfig = new JobConfigBuilder().withJobID("Id2").build();
		JobExecutor executor = new JobExecutor(jobConfig);
		ITask task = mock(ITask.class);
		ITask task1 = mock(ITask.class);
		ITask task2 = mock(ITask.class);
		doThrow(new Exception()).when(task2).run();
		List<ITask> tasks = new ArrayList<>();
		tasks.add(task);
		tasks.add(task1);
		tasks.add(task2);
		Future<ExecutionResult> result = executor.execute(tasks.iterator());
		assertTrue(result.get().isSuccessful() == failure);
		verify(task, times(onlyOneTime)).run();
		verify(task1, times(onlyOneTime)).run();
		verify(task2, times(onlyOneTime)).run();
		verify(task, times(onlyOneTime)).undo();
		verify(task1, times(onlyOneTime)).undo();
	}

	@Test
	public void testExecuteJobWithTasksOneOfThemTakesMoreTaskTimeoutShouldBeFailedAndRollbackExectuedTasks()
			throws Exception {
		long timeout = 1000;
		IJobConfig jobConfig = new JobConfigBuilder().withJobID("Id2").withTimeoutPerTask(timeout).build();
		JobExecutor executor = new JobExecutor(jobConfig);
		ITask task = mock(ITask.class);
		ITask task1 = new SleepingTask(timeout + 200);
		ITask spyTask = spy(task1);
		ITask task2 = mock(ITask.class);
		List<ITask> tasks = new ArrayList<>();
		tasks.add(task);
		tasks.add(spyTask);
		tasks.add(task2);
		Future<ExecutionResult> result = executor.execute(tasks.iterator());
		assertTrue(result.get().isSuccessful() == failure);
		verify(task, times(onlyOneTime)).run();
		verify(spyTask, times(onlyOneTime)).run();
		verify(task2, times(0)).run();
		verify(task, times(onlyOneTime)).undo();
	}

	@Test
	public void testTermianteJobWhileThirdTaskExecutionShouldBeTerminatedSuccessfullyAndFirstTwoTasksBeUndone() throws Exception {
		long timeout = 2000;
		IJobConfig jobConfig = new JobConfigBuilder().withJobID("Id2").withTimeoutPerTask(timeout).build();
		JobExecutor executor = new JobExecutor(jobConfig);
		ITask task = new SleepingTask(timeout - 250);
		ITask task1 = new SleepingTask(timeout - 250);
		ITask task2 = new SleepingTask(timeout - 250);

		ITask spyTask = spy(task);
		ITask spyTask1 = spy(task1);
		ITask spyTask2 = spy(task2);

		List<ITask> tasks = new ArrayList<>();
		tasks.add(spyTask);
		tasks.add(spyTask1);
		tasks.add(spyTask2);
		Thread executionThread = new Thread(new Runnable() {

			@Override
			public void run() {
				Future<ExecutionResult> result = executor.execute(tasks.iterator());
					try {
						result.get();
					} catch (InterruptedException | ExecutionException e) {
						/* Expected */
						return;
					}
			}
		});
		executionThread.start();

		Thread.currentThread().sleep(timeout * 2); // Wait until first two tasks be done

		Thread terminationThread = new Thread(new Runnable() {
			@Override
			public void run() {
				executor.terminate();
			}
		});
		terminationThread.start();
		terminationThread.join(); // Wait until termination thread is done
		executionThread.join(); // Wait until execution thread is done
		
		
		verify(spyTask, times(onlyOneTime)).run();
		verify(spyTask1, times(onlyOneTime)).run();
		verify(spyTask2, times(onlyOneTime)).run();
		verify(spyTask, times(onlyOneTime)).undo();
		verify(spyTask1, times(onlyOneTime)).undo();
		verify(spyTask2, times(0)).undo();
	}

}

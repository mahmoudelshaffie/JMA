package com.optile.jma.test;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import com.optile.jma.api.ITask;

public class TaskTestUtils {

	public static ITask getSuccessTask() {
		return mock(ITask.class); 
	}
	
	public static ITask getFailedTask() throws Exception {
		ITask task = mock(ITask.class);
		doThrow(new Exception()).when(task).run();
		return task;
	}
	
	public static ITask getSleepyTask(long time2sleep) {
		SleepingTask task = new SleepingTask(time2sleep);
		SleepingTask spiedTask = spy(task);
		return spiedTask;
	}
	
}

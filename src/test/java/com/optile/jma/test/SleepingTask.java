package com.optile.jma.test;

import com.optile.jma.api.ITask;
import com.optile.jma.api.TaskStatus;

public class SleepingTask implements ITask {

	private long time2sleep = 0;
	
	public SleepingTask(long time2sleep) {
		this.time2sleep = time2sleep;
	}
	
	@Override
	public void undo() {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() throws Exception {
		Thread.sleep(time2sleep);
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
	
}

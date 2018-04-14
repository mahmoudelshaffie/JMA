package com.optile.jma.execution;

import com.optile.jma.api.IJob;

public class JobRunner implements Runnable {

	private IJob job;
	
	public JobRunner(IJob job) {
		this.job = job;
	}
	
	@Override
	public void run() {
		job.execute();
	}

}

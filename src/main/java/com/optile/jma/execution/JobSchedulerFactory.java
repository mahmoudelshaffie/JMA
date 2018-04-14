package com.optile.jma.execution;

import com.optile.jma.api.IJob;
import com.optile.jma.config.apis.IScheduleConfig;
import com.optile.jma.execution.api.IJobScheduler;

public class JobSchedulerFactory {

	public static IJobScheduler createSchedule(IJob job, IScheduleConfig config) {
		IJobScheduler scheduler;
		
		if (config.isRepeatable()) {
			scheduler = new FixedRateRepeatableJobScheduler(job, config.getStartDelay(), config.getRepeatDealy(), config.getTimeunit());
		} else {
			scheduler = new OnlyOneTimeScheduler(job, config.getStartDelay(), config.getTimeunit());
		}
		
		return scheduler;
	}
	
}

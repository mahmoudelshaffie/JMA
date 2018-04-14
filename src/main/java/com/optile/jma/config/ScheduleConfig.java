package com.optile.jma.config;

import java.util.concurrent.TimeUnit;

import com.optile.jma.config.apis.IScheduleConfig;

public class ScheduleConfig implements IScheduleConfig {

	private TimeUnit unit;
	private long startDelay;
	private long repeatDelay;
	
	public ScheduleConfig(TimeUnit unit, long startDelay, long repeatDelay) {
		this.unit = unit;
		this.startDelay = startDelay;
		this.repeatDelay = repeatDelay;
	}

	@Override
	public boolean isRepeatable() {
		return repeatDelay > 0 ? true : false;
	}

	@Override
	public TimeUnit getTimeunit() {
		return unit;
	}

	@Override
	public long getStartDelay() {
		return startDelay;
	}

	@Override
	public long getRepeatDealy() {
		return repeatDelay;
	}

}

package com.optile.jma.config.apis;

import java.util.concurrent.TimeUnit;

public interface IScheduleConfig {
	boolean isRepeatable();
	TimeUnit getTimeunit();
	long getStartDelay();
	long getRepeatDealy();
}

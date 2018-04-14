package com.optile.jma;

import java.util.concurrent.TimeUnit;

public class TimeConfig {

	private long value;
	private TimeUnit unit;
	
	public TimeConfig(long value, TimeUnit unit) {
		this.value = value;
		this.unit = unit;
	}

	public long getValue() {
		return value;
	}

	public TimeUnit getUnit() {
		return unit;
	}
	
	
}

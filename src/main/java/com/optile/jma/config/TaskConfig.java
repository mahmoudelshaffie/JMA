package com.optile.jma.config;

import com.optile.jma.config.apis.ITaskConfig;

public class TaskConfig implements ITaskConfig {

	private String description;
	private String id;
	
	public TaskConfig(String description, String id) {
		this.description = description;
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public String getId() {
		return id;
	}
}

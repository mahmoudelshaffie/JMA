package com.optile.jma.config;

import com.optile.jma.config.apis.ITaskConfig;

public class TaskConfigBuilder {

	private String description;
	private String id;
	
	public TaskConfigBuilder withID(String ID) {
		this.id = ID;
		return this;
	}
	
	public TaskConfigBuilder withDescription(String description) {
		this.description = description;
		return this;
	}
	
	public ITaskConfig build() {
		return new TaskConfig(description, id);
	}
}

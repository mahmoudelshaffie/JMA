package com.optile.jma.api;

public interface ITask {
	String getID();
	String getDescription();
	void run() throws Exception;
	void undo();
	TaskStatus getStatus();
}

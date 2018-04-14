package com.optile.jma.api;

public interface ICommand {
	public void execute() throws Exception;
	public void undo();
}

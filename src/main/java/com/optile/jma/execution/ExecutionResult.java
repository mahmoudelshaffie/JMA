package com.optile.jma.execution;

public class ExecutionResult {
	private boolean success;
	private Exception error;
	
	public ExecutionResult() {
		success = true;
		error = null;
	}
	
	public ExecutionResult(Exception ex) {
		success = false;
		error = ex;
	}
	
	public boolean isSuccessful() {
		return this.success;
	}
	
	public Exception getError() {
		return error;
	}
}
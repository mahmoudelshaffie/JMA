package com.optile.jma.excecptions;

public class IllegalStateException extends RuntimeException {

	public IllegalStateException(String currentState) {
		super("Action Is Not Allowed From Current State: " + currentState);
	}
}

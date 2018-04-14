package com.optile.jma.excecptions;

public class IsNotTerminableException extends RuntimeException {
	public IsNotTerminableException() {
		super("Target instance instance is not terminable");
	}
}

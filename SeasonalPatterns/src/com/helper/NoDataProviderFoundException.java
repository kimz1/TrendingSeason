package com.helper;

public class NoDataProviderFoundException extends Exception{

	private static final long serialVersionUID = 8587875637243354153L;

	public NoDataProviderFoundException() {
		super("DataProvider can not be null.");
	}
	
	public NoDataProviderFoundException(String msg) {
		super(msg);
	}
}

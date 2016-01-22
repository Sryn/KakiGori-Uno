package com.acme.sa41.day1.web;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class SAGroupException extends Exception {

	public SAGroupException() {
		super();
	}

	public SAGroupException(String msg) {
		super(msg);
	}
	
}

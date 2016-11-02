package com.packtpub.wflydevelopment.chapter3.exception;


public class NotEnoughMoneyException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotEnoughMoneyException(String string) {
        super(string);
    }
}

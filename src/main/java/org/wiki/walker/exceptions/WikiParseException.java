package org.wiki.walker.exceptions;

public class WikiParseException extends Exception{

	public WikiParseException(){
		super();
	}
	
	public WikiParseException(String message){		
		super(message);
	}
}

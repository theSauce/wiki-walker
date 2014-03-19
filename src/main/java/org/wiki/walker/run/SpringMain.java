package org.wiki.walker.run;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		new ClassPathXmlApplicationContext( args[0] );
		
	}

}

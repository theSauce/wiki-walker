package org.wiki.walker.ui;

import java.util.Scanner;

import org.wiki.walker.WikiWalker;

public class WikiConsole {

	private WikiWalker walker;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		System.out.println("WIKI WALKER\n\nType in an article to start from:\n");
		
		Scanner in = new Scanner( System.in );
		
		while( in.hasNext() ){
			
			handleInput( in.nextLine(), in );
			//System.sleep(100);
		}
	}

	private static void handleInput(String nextLine, Scanner in) {
		
		if( nextLine != null ){
			
			if( "Exit".equalsIgnoreCase( nextLine ) ){
				in.close();
				System.exit(0);
			}
			
		}
		
	}

	public WikiWalker getWalker() {
		return walker;
	}

	public void setWalker(WikiWalker walker) {
		this.walker = walker;
	}

	
	
}

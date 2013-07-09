package org.wiki.walker.ui;

import java.util.Date;
import java.util.Scanner;

import org.wiki.walker.WikiWalker;

public class WikiConsole {

	private static WikiWalker walker;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		System.out.println("WIKI WALKER\n\nType in a start and end article, \"*\" separated:\n");
		
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
			
			String[] articles = nextLine.split("*");
			
			if( articles.length != 2 ){
				System.out.println("You done fucked up. Try again.");
				return;
			}
			
			Date start = new Date();
			String result = walker.getWalk( articles[0], articles[1]);
			
			if( result == null ){
				System.out.println("Not wiki articles. Try again.");
			} else {
				System.out.println(result + "\n" + (new Date().getTime() - start.getTime())/1000 + " seconds");
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

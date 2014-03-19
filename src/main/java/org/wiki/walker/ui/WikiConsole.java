package org.wiki.walker.ui;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.wiki.walker.WikiWalker;
import org.wiki.walker.format.WikiFormatter;

import de.tudarmstadt.ukp.wikipedia.api.exception.WikiApiException;

public class WikiConsole {

	private static WikiWalker walker;
	
	private static String start;
	private static String end;
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		new ClassPathXmlApplicationContext( args[0] );
		
		System.out.println("WIKI WALKER\n\nType in a starting article:");
		
		Scanner in = new Scanner( System.in );
		
		while( in.hasNext() ){
			
			try {
				handleInput( in.nextLine(), in );
			} catch (WikiApiException e) {
				e.printStackTrace();
				in.close();
				System.exit(0);
			}
		}
	}

	private static void handleInput(String nextLine, Scanner in) throws WikiApiException {
		
		if( nextLine != null ){
			
			if( "Exit".equalsIgnoreCase( nextLine ) ){
				in.close();
				System.exit(0);
			}
			
			if( "|Random|".equalsIgnoreCase( nextLine ) ){
				start = null;
				end = null;
				getRandomWikiWalk();
			}
			
			String formattedInput = WikiFormatter.asArticleName( nextLine );
			
			if( !walker.isTitle( formattedInput ) ){
				System.out.println("\n" + formattedInput + " is not an article. Did you mean one of these?\n");
				List<String> suggestions = walker.getArticleSuggestions( formattedInput );
				if( suggestions != null ) 
					for( String suggestion : suggestions )
						System.out.println( suggestion );
			} else if( walker.isDisambiguationPage( formattedInput ) ){ 
				System.out.println("\n" + formattedInput + " is a disambiguation page. Be more specific.");
			} else if( walker.isRedirect( formattedInput ) ){
				System.out.println("\n" + formattedInput + " is a redirect. I don't like those.");
			}else if( start == null ){
				start = formattedInput;
				System.out.println("\n\nEnter an ending article: ");
			} else {
				end = formattedInput;
				System.out.println("Wiki-walking...");
				Date startTime = new Date();
				String result = walker.getWalk( start, end);
				System.out.println(result + "\n" + 
						(new Date().getTime() - startTime.getTime())/1000 + " seconds");
				start = null;
				end = null;
				System.out.println("\n\nEnter a starting article: ");
			}
			
		}
		
	}
	
	private static void getRandomWikiWalk() throws WikiApiException{
		
		String start = null;
		
		while( !isValidArticle( start = walker.getRandomArticle() ) ){
			
		}
		
		String end = null;
		
		while( !isValidArticle( end = walker.getRandomArticle() ) ){
			
		}
		
		System.out.println("Wiki-walking from " + start + " to " + end + "...");
		Date startTime = new Date();
		String result = walker.getWalk( start, end);
		System.out.println(result + "\n" + 
				(new Date().getTime() - startTime.getTime())/1000 + " seconds");		
	}
	
	private static boolean isValidArticle( String article ) throws WikiApiException{
		return walker.isTitle( article ) && !walker.isDisambiguationPage( article ) && !walker.isRedirect( article);
	}

	public WikiWalker getWalker() {
		return walker;
	}

	public void setWalker(WikiWalker walker) {
		WikiConsole.walker = walker;
	}

	
	
}

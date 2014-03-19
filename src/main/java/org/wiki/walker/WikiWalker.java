package org.wiki.walker;

import java.util.List;

import de.tudarmstadt.ukp.wikipedia.api.exception.WikiApiException;

public interface WikiWalker {

	public boolean isTitle( String title );
	
	public boolean isDisambiguationPage( String title ) throws WikiApiException;
	
	public String getWalk( String start, String end);

	boolean isRedirect(String title) throws WikiApiException;
	
	public String getRandomArticle();
	
	public List<String> getArticleSuggestions( String string );
}

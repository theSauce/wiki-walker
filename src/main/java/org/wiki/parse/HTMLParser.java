package org.wiki.parse;

import java.util.Collection;

import org.wiki.exceptions.WikiParseException;


public interface HTMLParser {

	/**
	 * 
	 * @param html - The raw text constituting the html file
	 * @return The number of links pointing to other wikipedia articles in the bodyContent
	 * @throws WikiParseException 
	 */
	int getLinkCount(String html);
	
	/**
	 * 
	 * @param html - The raw text constituting the html file
	 * @return An array of Strings representing each link in the page
	 * @throws WikiParseException 
	 */
	Collection<String> getLinks(String html);
}

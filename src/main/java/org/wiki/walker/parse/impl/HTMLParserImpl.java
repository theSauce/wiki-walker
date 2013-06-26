package org.wiki.walker.parse.impl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Component;

import org.wiki.walker.exceptions.WikiParseException;
import org.wiki.walker.parse.HTMLParser;

@Component
public class HTMLParserImpl implements HTMLParser {

	private static final String BODYSTARTMARKER = "<!-- bodyContent -->";
	private static final String BODYENDMARKER = "<!-- /bodyContent -->";
	
	@Override
	public int getLinkCount(String html) {
		
		return this.getLinks(html).size();
	}
	
	@Override
	public Collection<String> getLinks(String html){
		
		int bodyStart = html.indexOf(BODYSTARTMARKER);
		int bodyEnd = html.lastIndexOf(BODYENDMARKER);
		if( bodyStart == -1 || bodyEnd == -1)
			try {
				throw new WikiParseException("The given page does not adhere to the " +
						"expected format (bodyContent div differs).");
			} catch (WikiParseException e) {
				e.printStackTrace();
			}
		
		bodyStart += BODYSTARTMARKER.length();
		
		String body = html.substring(bodyStart, bodyEnd);
		
		String[] linksArray = body.split("a href");
		
		List<String> links = new LinkedList<String>();
		for( String link : linksArray){
			if("/wiki/".equals(link.substring(2, 8))){
				links.add(link.substring(8, link.indexOf(" ") - 1));
			}
		}
		
		return links;
	}

	

}

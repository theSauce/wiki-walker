package org.wiki.walker;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.tudarmstadt.ukp.wikipedia.api.Page;
import de.tudarmstadt.ukp.wikipedia.api.Wikipedia;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiApiException;

//???? More Noose check

@Component
public class WikiWalker {

	private Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	private Wikipedia wiki;
	
	
	public String getWalk(String start, String end){
		
		logger.info( "Wiki-walking from " + start + " to " + end );
		
		if( start != null && end != null ){
		
			String walk = "";
			
			try {
				for( Page page : wiki.getPage( start ).getOutlinks() ){
					logger.info( page.getTitle() );
				}
			} catch (WikiApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return walk;
		
		}
		
		return null;
	}
	
	public String getMoonWalk( String start, String end ){
		return getWalk( end, start );
	}
	
	public void setWiki(Wikipedia wiki){
		this.wiki = wiki;
	}
}

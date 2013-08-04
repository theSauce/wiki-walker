package org.wiki.walker;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wiki.walker.collections.WikiQueue;


import de.tudarmstadt.ukp.wikipedia.api.Wikipedia;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiApiException;

//???? More Noose check

@Component
public class WikiWalker2 {

	private Log logger = LogFactory.getLog(getClass());
	
	private Map<Integer, Integer> shortestDistances = new HashMap<Integer, Integer>();
	
	private WikiQueue unsettled = new WikiQueue();
	
	private Set<Integer> settled = new HashSet<Integer>();
	
	private Map<Integer, Integer> predecessors = new HashMap<Integer, Integer>();
	
	@Autowired
	private Wikipedia wiki;
	
	
	public WikiWalker2(){}
	
	//TODO: this constructor is just for testing
	public WikiWalker2( Wikipedia wiki){
		this.wiki = wiki;
		Date start = new Date();
		System.out.println( getWalk("Michael_Jordan", "Paul_Silas") );
		System.out.println((new Date().getTime() - start.getTime())/1000 + " seconds");
	}
	
    private void init(int start)
    {
        settled.clear();
        unsettled.clear();
        
        shortestDistances.clear();
        predecessors.clear();
        
        WikiNode startNode = new WikiNode(start);
        
        setShortestDistance(startNode, 0);
        unsettled.add(startNode);
    }
	
	public String getWalk(String startString, String endString){
		
		logger.info( "Wiki-walking from " + startString + " to " + endString );
		
		if( startString != null && endString != null ){
		
			int start;
			int end;
			try {
				
				//System.out.println( wiki.getPage( startString ).getText() );
				
				start = wiki.getPage( startString ).getPageId();
				end = wiki.getPage( endString ).getPageId();	
				
				if( start == end ) return startString;
				
		        init(start);
		        
		        WikiNode current;
		        
		        while ( !unsettled.isEmpty() ) {
		        	
		        	current = unsettled.poll();
		        	
		        	if( current.getId() == end ){
		        		System.out.println();
		        	}
		        	
		            assert !settled.contains( current );
		            
		            if ( current.getId() == end ) break;
		            
		            settled.add( current.getId() );
		            
		            relaxNeighbors( current );
		            
		            unsettled.remove( current );
		        }
		
				int iterator = end;
				List<String> walk = new LinkedList<String>();
				while ( iterator != start ) {
					walk.add( wiki.getPage( iterator ).getTitle().toString() );
					iterator = predecessors.get( iterator );
				}
				walk.add( wiki.getPage( iterator ).getTitle().toString() );
				
				Collections.reverse( walk );
				
				return walk.toString();
				
			} catch (WikiApiException e) {
				
				logger.info( "Args supplied aren't wiki pages: " + startString + ", " + endString );
				
				return null;
			}
		}
		
		return null;
	}
	
	private int count = 0;
	
	private void relaxNeighbors(WikiNode id) throws WikiApiException {
		
		System.out.println(count++ + ".) Relaxing for: " + id);
		
        for ( int neighborId : wiki.getPage( id.getId() ).getOutlinkIDs() ) {
        
        	WikiNode neighbor = new WikiNode(neighborId);
        	//if( !wiki.getPage( neighbor ).isDisambiguation() ){
	        	
	        	if ( settled.contains( neighbor ) ) continue;
	            
	            int shortDist = getShortestDistance( id.getId() ) + 1;
	            
	            if (shortDist < getShortestDistance( neighbor.getId() ) )
	            {
	                setShortestDistance(neighbor, shortDist);
	                                
	                predecessors.put(neighbor.getId(), id.getId());
	            }
        	//}
        }        
    }
	
    private int getShortestDistance(int id) {
        Integer d = shortestDistances.get(id);
        return (d == null) ? Integer.MAX_VALUE : d;
    }
	
    private void setShortestDistance( WikiNode id, int distance ) {

    	id.setShortestDistance( distance );
    	
 //   	System.out.println("unsettled: "+unsettled+"\nid: " + id);
    	
    	unsettled.remove(id);
    	
//        if( unsettled.remove(id) ) {
//        	System.out.println("removed: " + id.getId() );        
//        } else {
//        	System.out.println("didn't remove: " + id.getId() );
//        }

        shortestDistances.put(id.getId(), distance);

        unsettled.add(id);
//        if( unsettled.add(id) ) {
//        	System.out.println("added: " + id.getId() );        
//        } else {
//        	System.out.println("didn't add: " + id.getId() );
//        }
        
//    	if( id.getId() == 4173915 ){
//    		System.out.println();
//    	}
    }
	
	
	
	public String getMoonWalk( String start, String end ){
		return getWalk( end, start );
	}
	
	public void setWiki(Wikipedia wiki){
		this.wiki = wiki;
	}
}

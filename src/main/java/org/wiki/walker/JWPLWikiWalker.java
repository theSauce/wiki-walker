package org.wiki.walker;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wiki.walker.collections.WikiQueue;


import de.tudarmstadt.ukp.wikipedia.api.Wikipedia;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiApiException;

/**
 * An implementation of Dijkstra's Shortest Path
 * 
 * @author Ahoy-hoy
 *
 */

@Component
public class JWPLWikiWalker implements WikiWalker {

	private Log logger = LogFactory.getLog(getClass());
	
	private Map<Integer, Integer> shortestDistances = new HashMap<Integer, Integer>();
	
	private WikiQueue unsettled = new WikiQueue();
	
	private Set<Integer> settled = new HashSet<Integer>();
	
	//private Map<Integer, Integer> predecessors = new HashMap<Integer, Integer>();
	private int[] predecessors = new int[40000000];
	
	@Autowired
	private Wikipedia wiki;
	
	
	public JWPLWikiWalker(){}
	
	//TODO: this constructor is just for testing
	public JWPLWikiWalker( Wikipedia wiki){
		this.wiki = wiki;
		Date start = new Date();
		System.out.println( getWalk("Samuel_L._Jackson", "Scientology") );
		System.out.println( (new Date().getTime() - start.getTime())/(1000) + " seconds");
	}
	
    private void init(int start)
    {
        settled.clear();
        unsettled.clear();
        
        shortestDistances.clear();
        //predecessors.clear();
        
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
		        int count = 1;
		        while ( !unsettled.isEmpty() ) {
		        	
		        	current = unsettled.poll();
		        	
		            assert !settled.contains( current );
		            
		            if ( current.getId() == end ) break;
		            
		            settled.add( current.getId() );
		            
		            relaxNeighbors( current );
		            
		            //do i need this?
		            unsettled.remove( current );
		            
		            if( count++%100 == 0 ){
		            	getStatus();
		            }
		        }
		
				int iterator = end;
				List<String> walk = new LinkedList<String>();
				while ( iterator != start ) {
					walk.add( wiki.getPage( iterator ).getTitle().toString() );
					iterator = predecessors[iterator];
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
	
	private void relaxNeighbors(WikiNode id) throws WikiApiException {
		
        for ( int neighborId : wiki.getPage( id.getId() ).getOutlinkIDs() ) {
        
        	WikiNode neighbor = new WikiNode(neighborId);
        	//if( !wiki.getPage( neighbor ).isDisambiguation() ){
	        	
	        	if ( settled.contains( neighbor ) ) continue;
	            
	            int shortDist = getShortestDistance( id.getId() ) + 1;
	            
	            if (shortDist < getShortestDistance( neighbor.getId() ) )
	            {
	                setShortestDistance(neighbor, shortDist);
	                                
	                //predecessors.put(neighbor.getId(), id.getId());
	                predecessors[neighbor.getId()] = id.getId();
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
    	
    	unsettled.remove(id);
    	
        shortestDistances.put(id.getId(), distance);

        unsettled.add(id);

    }
    
	private void getStatus() {
	
		logger.info( "********CURRENT STATUS********" );
		logger.info( new Date() );
		logger.info( "Settled: " + settled.size() );
		logger.info( "Unsettled: " + unsettled.size() );
		logger.info( "Contents:\n" + unsettled );
		
	}
	
	public String getMoonWalk( String start, String end ){
		return getWalk( end, start );
	}
	
	public void setWiki(Wikipedia wiki){
		this.wiki = wiki;
	}
}

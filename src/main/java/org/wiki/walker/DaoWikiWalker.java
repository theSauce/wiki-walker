/**
 * 
 */
package org.wiki.walker;

import gnu.trove.iterator.TIntIterator;
import gnu.trove.map.hash.TIntIntHashMap;
import gnu.trove.set.hash.TIntHashSet;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.wiki.walker.collections.PrimitiveWikiQueue;
import org.wiki.walker.collections.ShortestDistanceMap;
import org.wiki.walker.collections.WikiQueue;
import org.wiki.walker.dao.WikiDao;
import org.wiki.walker.dao.WikipediaBoneCPDao;
import org.wiki.walker.dao.WikipediaDao;

import de.tudarmstadt.ukp.wikipedia.api.exception.WikiApiException;

/**
 * @author Ahoy-hoy
 *
 */
@Component
public class DaoWikiWalker implements WikiWalker{

	//private WikipediaDao dao;
	private WikiDao dao;
	
	private Log logger = LogFactory.getLog(getClass());
	
	//private Map<Integer, Integer> shortestDistances = ShortestDistanceMap.getInstance().getShortestDistanceMap();
	private TIntIntHashMap shortestDistances = ShortestDistanceMap.getInstance().getShortestDistanceMap();
	
	private PrimitiveWikiQueue unsettled = new PrimitiveWikiQueue();
	
	//private Set<Integer> settled = new HashSet<Integer>();
	private TIntHashSet settled = new TIntHashSet();
	
	//private Map<Integer, Integer> predecessors = new HashMap<Integer, Integer>();
	//private int[] predecessors = new int[40000000];
	private TIntIntHashMap predecessors = new TIntIntHashMap();
	
	
	public DaoWikiWalker( WikiDao dao ) {
		this.dao = dao;
		Date start = new Date();
		System.out.println( getWalk("ToeJam_&_Earl_in_Panic_on_Funkotron", "Orchestrion") );
		System.out.println( (new Date().getTime() - start.getTime())/(1000) + " seconds");
	}
	
	private void init(int start) {
        settled.clear();
        unsettled.clear();
        
        shortestDistances.clear();
        predecessors.clear();
        
        setShortestDistance(start, 0);
        unsettled.add(start);
    }
	
	@Override
	public String getWalk(String startString, String endString) {
		
		logger.debug( "Wiki-walking from " + startString + " to " + endString );
		
		if( startString != null && endString != null ){
		
			int start;
			int end;
			try {
				
				//System.out.println( wiki.getPage( startString ).getText() );
				
				start = dao.getId( startString );
				end = dao.getId( endString );	
				
				if( start == -1 ){
					logger.info( startString + " not found as an article. Mispelled?" );
					return null;
				}
				
				if( end == -1 ){
					logger.info( endString + " not found as an article. Mispelled?" );
					return null;
				}
				
				if( start == end ) return startString;
				
		        init(start);
		        
		        int current;
		        int count = 1;
		        while ( !unsettled.isEmpty() ) {
		        	
		        	current = unsettled.poll();
		        	
		            assert !settled.contains( current );
		            
		            if ( current == end ) break;
		            
		            settled.add( current );
		            
		            int _break = relaxNeighbors( current, end );
		          
		            if( _break != -1 ){
		            	
		            	predecessors.put( end, current);
		            	break;
		            	
		            }
		            
		            //TODO: do i need this?
		            unsettled.remove( current );
		            
		            if( count++%100 == 0 ){
		            	getStatus();
		            }
		        }
		
				int walker = end;
				List<String> walk = new LinkedList<String>();
				while ( walker != start ) {
					walk.add( dao.getTitle( walker ) );
					walker = predecessors.get(walker);
				}
				walk.add( dao.getTitle( walker ) );
				
				Collections.reverse( walk );
				
				return walk.toString();
				
			} catch (WikiApiException e) {
				
				logger.debug( "Args supplied aren't wiki pages: " + startString + ", " + endString );
				
				return null;
			}
		}
		
		return null;
	}

	private int relaxNeighbors( int id, int end ) throws WikiApiException {
		
		int neighborId;
		
        for ( TIntIterator i = dao.getOutlinkIDs( id ).iterator(); i.hasNext(); ) {
        
        	neighborId = i.next();
	        	
        	if( neighborId == end ) return neighborId;
        	
        	if ( settled.contains( neighborId ) ) continue;
            
            int shortDist = getShortestDistance( id ) + 1;
            
            if (shortDist < getShortestDistance( neighborId ) )
            {
                setShortestDistance( neighborId, shortDist );
                                
                predecessors.put( neighborId, id );
            }
        }        
        
        return -1;
    }
	
    private int getShortestDistance(int id) {
        int d = shortestDistances.get(id);
        return (d == -1) ? Integer.MAX_VALUE : d;
    }
	
    private void setShortestDistance( int id, int distance ) {

    	//id.setShortestDistance( distance );
    	
    	unsettled.remove(id);
    	
        shortestDistances.put( id, distance );

        unsettled.add(id);

    }
    
	private void getStatus() {
	
		logger.info( "****CURRENT STATUS****" );
		//logger.info( new Date() );
		logger.info( "Settled: " + settled.size() );
		logger.info( "Unsettled: " + unsettled.size() );
		//logger.info( "Contents:\n" + unsettled );
		
	}
	
	public String getMoonWalk( String start, String end ){
		return getWalk( end, start );
	}
	
	
//	public WikipediaDao getDao() {
//		return dao;
//	}
//
//
//	public void setDao(WikipediaDao dao) {
//		this.dao = dao;
//	}

	public WikiDao getDao() {
		return dao;
	}


	public void setDao(WikiDao dao) {
		this.dao = dao;
	}
}

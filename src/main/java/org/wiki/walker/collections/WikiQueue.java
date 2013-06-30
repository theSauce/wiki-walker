package org.wiki.walker.collections;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wiki.walker.WikiNode;

/**
 * 
 * The odd needs of the Wiki-Walker required the creation of a new collection. The problem
 * with SortedSets is that we need to store articles in a sorted collection so operations
 * are efficient, but the compareTo() and equals() methods of these articles is inconsistent.
 * We'd like to sort the articles based on distance from the root node but identify equality
 * based on ID, and these two criteria are incompatible. TreeSet, for example, will not allow
 * more than one article in it with the same shortestDistance.
 * 
 * This implementation is the work around. It is final because it really isn't flexible or
 * extensible; it has a very specific purpose and its methods are tightly coupled.
 * 
 * @author Ahoy-hoy
 *
 */

public final class WikiQueue implements Ordered<WikiNode> {

	private Log logger = LogFactory.getLog(getClass());
	
	private Map<Integer, TreeSet<WikiNode>> buckets = new HashMap<Integer, TreeSet<WikiNode>>();
	
	private TreeSet<Integer> hashes = new TreeSet<Integer>();
	
	
	@Override
	public boolean contains( WikiNode node ){
		
		TreeSet<WikiNode> set = buckets.get( node.getShortestDistance() );
		
		if( set != null ){
			return set.contains( node );
		} 
		
		return false;
	}
	
	@Override
	public boolean add(WikiNode node) {
		
		if( node != null ){
	
			int distance = node.getShortestDistance();
			
			if( buckets.get( distance ) == null ){
				buckets.put( distance, new TreeSet<WikiNode>() );
				hashes.add( distance );
			}
			
			buckets.get( distance ).add( node );
			
			return true;
		}
		
		return false;
	}

	@Override
	public boolean remove(WikiNode node) {

		boolean removed = false;
		
		if( !hashes.isEmpty() ){
			
			int hashToRemove = -1;
			
			Iterator<Integer> iterator = hashes.iterator();
			
			while( iterator.hasNext() ){
				
				int n = iterator.next();
				
				TreeSet<WikiNode> set = buckets.get( n );
				
				if( set.remove( node ) ){
					
					if( set.size() == 0) {
						buckets.remove( n );
						hashToRemove = n;		//don't want concurrent modification errors
					}
					
					removed = true;
				}
				
			}
			
			if( hashToRemove >= 0 ) hashes.remove( hashToRemove );
			
		}
		
		return removed;
	}

	@Override
	public WikiNode poll() {
		
		if( !hashes.isEmpty() ){
			
			Iterator<Integer> i = hashes.iterator();
			
			while( i.hasNext() ){
				
				int n = i.next();
				
				TreeSet<WikiNode> set = buckets.get( n );
				
				//TODO: for cleanup, but is it necessary?
				if( set == null ){
					
					hashes.remove( n );
					buckets.remove( n );
					
				} else {
					
					//TODO: will this ever throw a NPE?
					return set.first();
					
				}
				
			}
			
		}
		
		return null;
	}

	@Override
	public void clear() {
		buckets.clear();
		hashes.clear();
	}

	@Override
	public boolean isEmpty() {
		
		Iterator<Integer> iterator = hashes.iterator();
		
		while( iterator.hasNext() ){
			
			if( !buckets.get( iterator.next() ).isEmpty() ){
				return false;
			}
			
		}
		
		return true;
	}


	public int size(){
		
		int sum = 0;
		Iterator<Integer> i = hashes.iterator();
		
		while( i.hasNext() ){
			
			sum += buckets.get( i.next() ).size();
			
		}
		
		return sum;
	}
	
	@Override
	public String toString(){
		
		String result = "";
		Iterator<Integer> i = hashes.iterator();
		
		while( i.hasNext() ){
			
			int hash = i.next();
			
			result += hash + ": " + buckets.get( hash ) + "\n";
		}
		
		return result;
	}

}

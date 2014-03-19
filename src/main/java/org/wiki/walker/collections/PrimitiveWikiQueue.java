package org.wiki.walker.collections;

import gnu.trove.map.hash.TIntIntHashMap;

import it.unimi.dsi.fastutil.ints.IntAVLTreeSet;
import it.unimi.dsi.fastutil.ints.IntSortedSet;

import java.util.Iterator;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

public final class PrimitiveWikiQueue /*implements Ordered<Integer>*/ {

	private Log logger = LogFactory.getLog(getClass());
	
	private static final int MAX_DEGREES = 11;
	
	private TIntIntHashMap shortestDistances = ShortestDistanceMap.getInstance().getShortestDistanceMap();
	
	private IntSortedSet[] distanceBuckets = new IntSortedSet[MAX_DEGREES];
	
	private TreeSet<Integer> distances = new TreeSet<Integer>();
	
	private int _size = 0;
	
	//@Override
	public boolean contains( int id ){
		
		IntSortedSet set = distanceBuckets[shortestDistances.get( id )];
		
		if( set != null ){
			return set.contains( id );
		} 
		
		return false;
	}
	
	//@Override
	public boolean add( int id ) {

		int distance = shortestDistances.get( id );
		
		if( distanceBuckets[distance] == null ){
			distanceBuckets[distance] = new IntAVLTreeSet();	//this turns out being a great heuristic (the lower the ID, the more common the article)
			distances.add( distance );
		}
		
		distanceBuckets[distance].add( id );
		_size++;
		
		return true;
	
	}

	//@Override
	public boolean remove( int id ) {
		
		boolean removed = false;
		
		if( !distances.isEmpty() ){
			
			int hashToRemove = -1;
			
			IntSortedSet set;
			for( int i = distances.first(); i < MAX_DEGREES; i++ ){
				
				set = distanceBuckets[i];
				
				if( set != null && set.remove( id ) ){
				
					_size--;
					
					if( set.size() == 0) {
						distanceBuckets[i] = null;
						hashToRemove = i;		//don't want concurrent modification errors
					}
					
					removed = true;
				}
				
			}
			
			if( hashToRemove >= 0 ) distances.remove( hashToRemove );
			
		}
		
		return removed;
	}

	//@Override
	public int poll() {
		
		if( !distances.isEmpty() ){
			
			IntSortedSet set;
			for( int i = distances.first(); i < MAX_DEGREES; i++ ){
				
				set = distanceBuckets[i];
				
				//TODO: for cleanup, but is it necessary?
				if( set == null ){
					
					distances.remove( i );
					distanceBuckets[i] = null;
					
				} else {
					
					return set.first();
				}
				
			}
			
		}
		
		return -1;
		
	}

	//@Override
	public void clear() {
		distanceBuckets = new IntAVLTreeSet[MAX_DEGREES];
		_size = 0;
		distances.clear();
	}

	//@Override
	public boolean isEmpty() {
		return _size < 1;
	}


	public int size(){
		return _size;
	}
	
	@Override
	public String toString(){
		
		String result = "";
		Iterator<Integer> i = distances.iterator();
		
		while( i.hasNext() ){
			
			int hash = i.next();
			
			result += hash + ": " + distanceBuckets[hash].size() + "\n";
		}
		
		return result;
	}

}

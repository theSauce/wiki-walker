package org.wiki.walker.collections;

import gnu.trove.map.hash.TIntIntHashMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.hash.TIntHashSet;

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

public final class PrimitiveWikiQueue /*implements Ordered<Integer>*/ {

	private Log logger = LogFactory.getLog(getClass());
	
	private TIntIntHashMap shortestDistances = ShortestDistanceMap.getInstance().getShortestDistanceMap();
	
	//private Map<Integer, TreeSet<WikiNode>> buckets = new HashMap<Integer, TreeSet<WikiNode>>();
	private TIntObjectHashMap<TreeSet<Integer>> buckets = new TIntObjectHashMap<TreeSet<Integer>>();
	//private TIntObjectHashMap<TIntHashSet> buckets = new TIntObjectHashMap<TIntHashSet>();
	
	private TreeSet<Integer> hashes = new TreeSet<Integer>();
	
	private static final int MAX_DEGREES = 11;
	
	//TODO: think of different implementation for hash keys. boolean array?
	
	
	//@Override
	public boolean contains( int id ){
		
		TreeSet<Integer> set = buckets.get( shortestDistances.get( id ) );
		//TIntHashSet set = buckets.get( shortestDistances.get( id ) );
		
		if( set != null ){
			return set.contains( id );
		} 
		
		return false;
	}
	
	//@Override
	public boolean add( int id ) {

		int distance = shortestDistances.get( id );
		
		if( buckets.get( distance ) == null ){
			buckets.put( distance, new TreeSet<Integer>() );
			//buckets.put( distance, new TIntHashSet() );
			hashes.add( distance );
		}
		
		buckets.get( distance ).add( id );
		
		return true;
	
	}

	//@Override
	public boolean remove( int id ) {

//		boolean removed = false;
//		
//		if( !hashes.isEmpty() ){
//			
//			int hashToRemove = -1;
//			
//			Iterator<Integer> iterator = hashes.iterator();
//			
//			while( iterator.hasNext() ){
//				
//				int n = iterator.next();
//				
//				TreeSet<Integer> set = buckets.get( n );
//				//TIntHashSet set = buckets.get( n );
//				
//				if( set.remove( id ) ){
//					
//					if( set.size() == 0) {
//						buckets.remove( n );
//						hashToRemove = n;		//don't want concurrent modification errors
//					}
//					
//					removed = true;
//				}
//				
//			}
//			
//			if( hashToRemove >= 0 ) hashes.remove( hashToRemove );
//			
//		}
//		
//		return removed;
		
		boolean removed = false;
		
		if( !hashes.isEmpty() ){
			
			int hashToRemove = -1;
			
			for( int i = hashes.first(); i < MAX_DEGREES; i++ ){
				
				TreeSet<Integer> set = buckets.get( i );
				//TIntHashSet set = buckets.get( n );
				
				if( set != null && set.remove( id ) ){
					
					if( set.size() == 0) {
						buckets.remove( i );
						hashToRemove = i;		//don't want concurrent modification errors
					}
					
					removed = true;
				}
				
			}
			
			if( hashToRemove >= 0 ) hashes.remove( hashToRemove );
			
		}
		
		return removed;
	}

	//@Override
	public int poll() {
		
//		if( !hashes.isEmpty() ){
//			
//			Iterator<Integer> i = hashes.iterator();
//			
//			while( i.hasNext() ){
//				
//				int n = i.next();
//				
//				TreeSet<Integer> set = buckets.get( n );
//				//TIntHashSet set = buckets.get( n );
//				
//				//TODO: for cleanup, but is it necessary?
//				if( set == null ){
//					
//					hashes.remove( n );
//					buckets.remove( n );
//					
//				} else {
//					
//					//TODO: will this ever throw a NPE?
//					return set.first();
//					//return set.iterator().next();
//				}
//				
//			}
//			
//		}
//		
//		return -1;
		
		if( !hashes.isEmpty() ){
			
			for( int i = hashes.first(); i < MAX_DEGREES; i++ ){
				
				TreeSet<Integer> set = buckets.get( i );
				//TIntHashSet set = buckets.get( n );
				
				//TODO: for cleanup, but is it necessary?
				if( set == null ){
					
					hashes.remove( i );
					buckets.remove( i );
					
				} else {
					
					//TODO: will this ever throw a NPE?
					return set.first();
					//return set.iterator().next();
				}
				
			}
			
		}
		
		return -1;
		
	}

	//@Override
	public void clear() {
		buckets.clear();
		hashes.clear();
	}

	//@Override
	public boolean isEmpty() {
		
//		Iterator<Integer> iterator = hashes.iterator();
//		
//		while( iterator.hasNext() ){
//			
//			if( !buckets.get( iterator.next() ).isEmpty() ){
//				return false;
//			}
//			
//		}
		//TODO: should i bother checking more than 11 degrees?
		for( int i = hashes.first(); i < MAX_DEGREES; i++){
			
			TreeSet<Integer> set = buckets.get( i );
			
			if( set != null && !set.isEmpty() ){
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

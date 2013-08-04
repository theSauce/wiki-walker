package org.wiki.walker.collections;

import gnu.trove.map.hash.TIntIntHashMap;

import java.util.HashMap;
import java.util.Map;

public class ShortestDistanceMap {

	private static ShortestDistanceMap singleton;
	
	//private Map<Integer, Integer> shortestDistances = new HashMap<Integer, Integer>();
	private TIntIntHashMap shortestDistances = new TIntIntHashMap(10, 0.5f, -1, -1);
	
	private ShortestDistanceMap(){/*Singleton*/}
	
	public static ShortestDistanceMap getInstance(){
		
		if( singleton == null ){
			singleton = new ShortestDistanceMap();
		}
		return singleton;
		
	}
	
	public TIntIntHashMap getShortestDistanceMap(){
		return shortestDistances;
	}
}

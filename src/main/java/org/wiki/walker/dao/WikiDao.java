package org.wiki.walker.dao;

import java.util.List;

import gnu.trove.set.hash.TIntHashSet;

public interface WikiDao {

	public int getId( String title );
	
	public String getTitle( int id );
	
	public TIntHashSet getOutlinkIDs( int id );
	
	public List<String> getNamesByTokens( List<String> tokens );
}

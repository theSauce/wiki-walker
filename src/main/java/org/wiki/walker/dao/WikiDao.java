package org.wiki.walker.dao;

import gnu.trove.set.hash.TIntHashSet;

public interface WikiDao {

	public int getId( String title );
	
	public String getTitle( int id );
	
	public TIntHashSet getOutlinkIDs( int id );
}

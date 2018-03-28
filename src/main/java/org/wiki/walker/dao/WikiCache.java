/**
 * 
 */
package org.wiki.walker.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jolbox.bonecp.BoneCP;

import gnu.trove.set.hash.TIntHashSet;

/**
 * @author Suppressive Person
 *
 */
public class WikiCache extends WikipediaBoneCPDao{

	private static final String GET_OUTLINKS = "SELECT id, outLinks FROM wikipedia.page_outlinks";

	private Log logger = LogFactory.getLog(getClass());
	
	private Map<Integer, TIntHashSet> outlinks = new ConcurrentHashMap<>();
	
	
	public WikiCache( BoneCP boneCP ) throws SQLException{
		this.boneCP = boneCP;
		
		logger.info( "Populating outlinks cache..." );
		
		Connection conn = boneCP.getConnection();
		PreparedStatement ps = conn.prepareStatement( GET_OUTLINKS );
		ResultSet rs = ps.executeQuery();
		
		while( rs.next() ){
			int id = rs.getInt( 1 );
			TIntHashSet links = outlinks.get( id );
			if( links == null ){
				links = new TIntHashSet();
				outlinks.put( id, links );
			}
			links.add( rs.getInt( 2 ) );
		}
		
		logger.info( "Populated outlinks cache." );
	}
	
	public TIntHashSet getOutlinkIDs( int id ){
		return outlinks.get( id );
	}
}

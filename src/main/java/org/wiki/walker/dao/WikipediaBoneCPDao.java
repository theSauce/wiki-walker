/**
 * 
 */
package org.wiki.walker.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import gnu.trove.set.hash.TIntHashSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.jolbox.bonecp.BoneCP;

/**
 * @author Ahoy-hoy
 *
 */
public class WikipediaBoneCPDao implements WikiDao{

	private Log logger = LogFactory.getLog(getClass());
	
	private BoneCP boneCP;
	
	private static final String GET_ID = "select id from wikipedia.page where name = ?";
	private static final String GET_TITLE = "select name from wikipedia.page where id = ?";
	private static final String GET_OUTLINKS = "SELECT outLinks FROM wikipedia.page_outlinks where id = ?";
	
	//for testing
//	public WikipediaDao( JdbcTemplate template ){
//		this.template = template;
//		System.err.println( getOutlinks(87530) );
//	}
	
	private PreparedStatement getId;
	public int getId( String title ){
	
		try {
			Connection conn = boneCP.getConnection();
			getId = conn.prepareStatement( GET_ID );
			getId.setString( 1, title);
			ResultSet rs = getId.executeQuery();
			
			int id = -1;
			if( !rs.next() ) return id;
		
			id = rs.getInt( 1 );
		
			conn.close();
			
			return id;
		} catch (SQLException e){
			e.printStackTrace();
		}
		return -1;
	}
	
	private PreparedStatement getTitle;
	public String getTitle( int id ){

		try {
			Connection conn = boneCP.getConnection();
			getTitle = conn.prepareStatement( GET_TITLE );
			getTitle.setInt( 1, id);
			ResultSet rs = getTitle.executeQuery();
			
			String title = null;
			if( !rs.next() ) return title;
			
			title = rs.getString( 1 );	
			
			conn.close();
			
			return title;
		} catch (SQLException e){
			e.printStackTrace();
		}
		return null;
	}

	private PreparedStatement getOutlinkIDs;
	public TIntHashSet getOutlinkIDs( int id ){

		try {
			Connection conn = boneCP.getConnection();
			getOutlinkIDs = conn.prepareStatement( GET_OUTLINKS );
			getOutlinkIDs.setInt( 1, id);
			ResultSet rs = getOutlinkIDs.executeQuery();
			
			TIntHashSet outlinks = new TIntHashSet();
			while( rs.next() ) {
				
				outlinks.add( rs.getInt( 1 ) );
				
			}
			conn.close();	
			return outlinks;			
		
		} catch (SQLException e){
			e.printStackTrace();
		}
		return null;
	}

	public BoneCP getBoneCP() {
		return boneCP;
	}

	public void setBoneCP(BoneCP boneCP) {
		this.boneCP = boneCP;
	}
	
	
	

}

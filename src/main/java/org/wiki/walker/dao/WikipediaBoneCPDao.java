/**
 * 
 */
package org.wiki.walker.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import gnu.trove.set.hash.TIntHashSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.wiki.walker.WikiUtils;

import com.jolbox.bonecp.BoneCP;

/**
 * @author Ahoy-hoy
 *
 */
public class WikipediaBoneCPDao implements WikiDao{

	private Log logger = LogFactory.getLog(getClass());
	
	private BoneCP boneCP;
	
	private static final String SQL_WILDCARD = "%";
	private static final String GET_NAMES_SELECT_CLAUSE = "SELECT name FROM wikipedia.page";
	private static final String GET_ID = "select id from wikipedia.page where name = ?";
	private static final String GET_TITLE = GET_NAMES_SELECT_CLAUSE + " where id = ?";
	private static final String GET_OUTLINKS = "SELECT outLinks FROM wikipedia.page_outlinks where id = ?";
	
	private PreparedStatement getId;
	public int getId( String title ){
	
		try {
			Connection conn = boneCP.getConnection();
			getId = conn.prepareStatement( GET_ID );
			getId.setString( 1, title);
			ResultSet rs = getId.executeQuery();
			
			int id = -1;
			if( !rs.next() ) {
				conn.close();
				return id;
			}
		
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
			if( !rs.next() ) {
				conn.close();
				return title;
			}
			
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

	public List<String> getNamesByTokens(List<String> tokens) {
		
		if( WikiUtils.hasAValue( tokens ) ){
			
			logger.info("Searching for possible article names using the tokens: " + tokens );
			
			StringBuilder sql = new StringBuilder(GET_NAMES_SELECT_CLAUSE + " where name like '");
			
			for( String token : tokens ){
				if( token != null && token.trim().length() > 0){
					sql.append(token);
					sql.append(SQL_WILDCARD);
				}
			}
			sql.append("'");
		
			logger.info("Searching with SQL: " + sql );
			
			try {
				Connection conn = boneCP.getConnection();
				PreparedStatement statement = conn.prepareStatement(sql.toString());
				ResultSet rs = statement.executeQuery();
				
				List<String> names = new ArrayList<String>();
				while( rs.next() ){
					names.add( rs.getString(1) );
				}
				
				conn.close();
				
				logger.info("Found names: " + names);
				return names;
				
			} catch (SQLException e){
				e.printStackTrace();
			}
		} 
		
		logger.info("Requested to search for possible article names with NULL tokens.");
		
		return null;
	}
	
	public BoneCP getBoneCP() {
		return boneCP;
	}

	public void setBoneCP(BoneCP boneCP) {
		this.boneCP = boneCP;
	}

}

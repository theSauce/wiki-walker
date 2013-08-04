/**
 * 
 */
package org.wiki.walker.dao;

import gnu.trove.set.hash.TIntHashSet;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 * @author Ahoy-hoy
 *
 */
public class WikipediaDao implements WikiDao{

	private JdbcTemplate template;
	
	private static final String GET_ID = "select id from wikipedia.page where name = ?";
	private static final String GET_TITLE = "select name from wikipedia.page where id = ?";
	private static final String GET_OUTLINKS = "SELECT outLinks FROM wikipedia.page_outlinks where id = ?";
	
	//for testing
//	public WikipediaDao( JdbcTemplate template ){
//		this.template = template;
//		System.err.println( getOutlinks(87530) );
//	}
	
	public int getId( String title ){
	
		SqlRowSet rs = template.queryForRowSet( GET_ID, new Object[]{ title } );
		
		int id = -1;
		if( !rs.next() ) return id;
	
		id = rs.getInt( 1 );
		
		return id;
		
	}
	
	public String getTitle( int id ){

		SqlRowSet rs = template.queryForRowSet( GET_TITLE, new Object[]{ id } );
		
		String title = null;
		if( !rs.next() ) return title;
		
		title = rs.getString( 1 );	
		
		return title;
		
	}
	
	public TIntHashSet getOutlinkIDs( int id ){

		SqlRowSet rs = template.queryForRowSet( GET_OUTLINKS, new Object[]{ id } );
		
		TIntHashSet outlinks = new TIntHashSet();
		while( rs.next() ) {
			
			outlinks.add( rs.getInt( 1 ) );
			
		}
		
		return outlinks;

	}
	
	
	
	public JdbcTemplate getTemplate() {
		return template;
	}

	public void setTemplate(JdbcTemplate template) {
		this.template = template;
	}
}

/**
 * 
 */
package org.wiki.walker.format;

import java.util.Arrays;
import java.util.List;

import org.wiki.walker.WikiUtils;

/**
 * @author Ahoy-hoy
 *
 */
public class WikiFormatter {

	public static String asArticleName( String string ){
		String name = "";
		if( string != null && string.trim().length() > 0 ){
			while( string.contains( "  " ) ){
				string.replace("  ", " ");
			}
			name = string.replace(" ", "_");
		}
		return name;
	}
	
	public static List<String> tokenizeArticleName( String article ){
		if( article != null && article.trim().length() > 0 ){
			String[] tokens = article.split("_");
			return WikiUtils.removeNullAndEmpty( Arrays.asList(tokens) );
		}
		return Arrays.asList(new String[]{article});
	}
}

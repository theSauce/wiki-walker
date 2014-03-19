/**
 * 
 */
package org.wiki.walker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Ahoy-hoy
 *
 */
public class WikiUtils {

	public static boolean hasAValue( Collection<String> stringList ){
		if( stringList != null ){
			for( String string : stringList ){
				if( string != null && string.trim().length() > 0 ){
					return true;
				}
			}
		}
		return false;
	}
	
	public static List<String> removeNullAndEmpty( List<String> strings ){
		if( strings != null ){
			List<String> filteredList = new ArrayList<String>();
			for( String string : strings ){
				if( string != null && string.trim().length() > 0 )
					filteredList.add( string );
			}
			return filteredList.size() > 0 ? filteredList : strings;
		}
		return strings;
	}
}

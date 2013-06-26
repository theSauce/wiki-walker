package org.wiki.walker.parse.impl;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.wiki.walker.parse.impl.HTMLParserImpl;

public class HTMLParserImplTest {

	private HTMLParserImpl parser = new HTMLParserImpl();
	private String html = "";
	private boolean setUpFlag = false;
	
	
	@Before
	public void startUp(){
		
		if( setUpFlag ) return;
		
		File testFile = 
				new File("src/test/java/org/wiki/parse/impl/test.html");
		
		try {
			html = FileUtils.readFileToString(testFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		setUpFlag = true;
	}
	
	@Test
	public void testGetLinkCount(){
	
		Assert.assertEquals(167, parser.getLinkCount(html));
	}
	
	@Test
	public void testGetLinks(){
		
		for(String link : parser.getLinks(html)){
			//Assert.assertEquals(true, link.matches( "^/wiki/.*" ));
			System.out.println(link);
		}
	}
}

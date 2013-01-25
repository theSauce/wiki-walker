package org.wiki;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"WikiWalkerTest-config.xml"})
public class WikiWalkerTests {

	@Autowired
	private WikiWalker walker;
	
	@Before
	public void setUp() throws Exception {

	}
	
	@Test
	public void testGetHTML(){
		System.out.println( walker.getHTML("http://en.wikipedia.org/wiki/Wiki"));
	}
	
	@After
	public void tearDown() throws Exception {
	}

	public void setWalker(WikiWalker walker){
		this.walker = walker;
	}
}

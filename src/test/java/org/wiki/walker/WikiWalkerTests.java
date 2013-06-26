package org.wiki.walker;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.wiki.walker.WikiWalker;

import de.tudarmstadt.ukp.wikipedia.api.DatabaseConfiguration;
import de.tudarmstadt.ukp.wikipedia.api.Wikipedia;
import de.tudarmstadt.ukp.wikipedia.api.WikiConstants.Language;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiInitializationException;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"WikiWalkerTest-config.xml"})
public class WikiWalkerTests {

	//@Autowired
	private WikiWalker walker = new WikiWalker();
	
	@Before
	public void setUp() throws Exception {

	}
	
	@Test
	public void testGetWalk(){
        DatabaseConfiguration dbConfig = new DatabaseConfiguration();
        dbConfig.setHost("127.0.0.1");
        dbConfig.setDatabase("Wikipedia");
        dbConfig.setUser("root");
        dbConfig.setPassword("");
        dbConfig.setLanguage(Language.english);

        try {
			walker.setWiki( new Wikipedia(dbConfig) );
		} catch (WikiInitializationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println( walker.getWalk( "Michael_Jordan", "Chicago_Bulls" ) );
	}
	
	@After
	public void tearDown() throws Exception {
	}

	public void setWalker(WikiWalker walker){
		this.walker = walker;
	}
}

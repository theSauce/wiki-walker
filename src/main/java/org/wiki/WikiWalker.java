package org.wiki;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.wiki.parse.HTMLParser;

//???? More Noose check

@Component
public class WikiWalker {

	@Autowired
	private HTMLParser parser;
	
	
	public String getWalk(){
		//TODO: implement
		String walk = "";
		
//		for(String link : parser.getLinks(html)){
//			walk += (link + "\n");
//		}
		
		return walk;
	}
	
	public String getHTML(String link){
		
		URL url;
		URLConnection connection;
		BufferedReader reader;
		String line = null, data = "";
		try {
			url = new URL( link );
			connection = url.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(80)));
			reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			while ((line = reader.readLine()) != null) {
				data += line + "\n";
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}


		return data;
	}
	
	public void setParser(HTMLParser parser){
		this.parser = parser;
	}
}

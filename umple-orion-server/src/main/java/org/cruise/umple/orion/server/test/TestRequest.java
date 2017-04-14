package org.cruise.umple.orion.server.test;

import static org.junit.Assert.*;

import org.cruise.umple.orion.server.Request;
import org.junit.Test;
import org.junit.runners.JUnit4;

public class TestRequest {

	private static final String ORION_SERVER_WORKSPACE = "/opt/eclipse/workspace/";
	private static final String REQ_SPECIAL_CHARS = "Java\n/file/goon-OrionContent/My%20First%20Car/B!rdW@tch3r-S%3CYM%5E77.ump";
	private static final String REQ_MINIMAL = "\n/file/a-OrionContent/b/c.ump";
	
	@Test
	public void testLanguageParse(){
		Request req = new Request(REQ_SPECIAL_CHARS, ORION_SERVER_WORKSPACE);
		assertEquals(req.getLanguage(), "Java");
	}
	
	@Test
	public void testNullLanguage(){
		Request req = new Request(REQ_MINIMAL, ORION_SERVER_WORKSPACE);
		assertEquals(req.getLanguage(), "");
	}
	
	@Test
	public void testUsernameParse() {
		Request req = new Request(REQ_SPECIAL_CHARS, ORION_SERVER_WORKSPACE);
		assertEquals(req.getUsername(), "goon");
	}
	
	@Test
	public void testFilenameParse() {
		Request req = new Request(REQ_MINIMAL, ORION_SERVER_WORKSPACE);
		assertEquals(req.getFilename(), "/b/c.ump");
	}
	
	@Test
	public void testURLDecoding() {
		Request req = new Request(REQ_SPECIAL_CHARS, ORION_SERVER_WORKSPACE);
		assertEquals(req.getFilename(), "/My First Car/B!rdW@tch3r-S<YM^77.ump");
	}
	
	@Test
	public void testUserDirectory() {
		Request req = new Request(REQ_SPECIAL_CHARS, ORION_SERVER_WORKSPACE);
		assertEquals(req.getUserDirectory(), ORION_SERVER_WORKSPACE+"/go/goon/");
		
	}
	
	

}

package org.cruise.umple.orion.server;

import static org.junit.Assert.*;

import org.junit.Test;

public class RequestTest {

	private static final String ORION_SERVER_WORKSPACE = "/opt/eclipse/workspace/";
	private static final String REQ_SPECIAL_CHARS = "Java\n/file/goon-OrionContent/My%20First%20Car/B!rdW@tch3r-S%3CYM%5E77.ump";
	private static final String REQ_MINIMAL = "\n/file/aaa-OrionContent/b/c.ump"; // username must have 3 characters
	
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
	public void testFilenameParse() {
		Request req = new Request(REQ_MINIMAL, ORION_SERVER_WORKSPACE);
		assertEquals(req.getAbsoluteFilename(), ORION_SERVER_WORKSPACE + "/aa/aaa/OrionContent/b/c.ump");
	}
	
	@Test
	public void testFilenameParseSpecialCharacters() {
		Request req = new Request(REQ_SPECIAL_CHARS, ORION_SERVER_WORKSPACE);
		assertEquals(req.getAbsoluteFilename(), ORION_SERVER_WORKSPACE + "/go/goon/OrionContent/My First Car/B!rdW@tch3r-S<YM^77.ump");
	}	

}

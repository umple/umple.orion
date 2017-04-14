package org.cruise.umple.orion.server;

import static org.junit.Assert.*;

import org.junit.Test;

public class ArgGenTest {

	private static final String ORION_SERVER_WORKSPACE = "/opt/eclipse/workspace/";
	private static final String REQ_SPECIAL_CHARS = "Java\n/file/goon-OrionContent/My%20First%20Car/B!rdW@tch3r-S%3CYM%5E77.ump";
	private static final String REQ_MINIMAL = "\n/file/aaa-OrionContent/b/c.ump"; // username must have 3 characters
	private static final String REQ = "Php\n/file/nwam-OrionContent/road/to/dar/el/salaam.ump"; 
	
	@Test
	public void testArgGen() {
		Request req = new Request(REQ, ORION_SERVER_WORKSPACE);
		ArgGen argGen = new ArgGen(req);
		assertEquals("-g Php --path src-Php/ " + ORION_SERVER_WORKSPACE + "/nw/nwam/OrionContent/road/to/dar/el/salaam.ump"
				, argGen.toString());
	}
	
	// Needs to go into a "src" folder, not just the same folder as the source
	// see issues #11
	@Test
	public void testArgGenWithNullLanguage(){
		Request req = new Request(REQ_MINIMAL, ORION_SERVER_WORKSPACE);
		ArgGen argGen = new ArgGen(req);
		assertEquals(ORION_SERVER_WORKSPACE + "/aa/aaa/OrionContent/b/c.ump",
				argGen.generateUmpleArgs()[0]);
	}
	
	@Test
	public void testArgGenSpecialCharacters() {
		Request req = new Request(REQ_SPECIAL_CHARS, ORION_SERVER_WORKSPACE);
		ArgGen argGen = new ArgGen(req);
		assertEquals("-g Java --path src-Java/ " + ORION_SERVER_WORKSPACE + "/go/goon/OrionContent/My First Car/B!rdW@tch3r-S<YM^77.ump"
				, argGen.toString());
	}

}

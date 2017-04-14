package org.cruise.umple.orion.server.test;

import static org.junit.Assert.*;

import org.cruise.umple.orion.server.ArgGen;
import org.cruise.umple.orion.server.Request;
import org.junit.Test;

public class TestArgGen {
	
	private static final String ORION_SERVER_WORKSPACE = "/opt/eclipse/workspace/";
	private static final String REQ_SPECIAL_CHARS = "Java\n/file/goon-OrionContent/My%20First%20Car/B!rdW@tch3r-S%3CYM%5E77.ump";
	private static final String REQ_MINIMAL = "\n/file/aaa-OrionContent/b/c.ump"; // username must have 3 characters
	
	@Test
	public void testArgGen() {
		
	}
	
	@Test
	public void testArgGenWithNullLanguage(){
		Request req = new Request(REQ_MINIMAL, ORION_SERVER_WORKSPACE);
		ArgGen argGen = new ArgGen(req);
		assertEquals(argGen.generateUmpleArgs()[0], ORION_SERVER_WORKSPACE + "/aa/aaa/OrionContent/b/c.ump");
	}
	
	@Test
	public void testArgGenSpecialCharacters() {
		// FORMAT NEEDS TO BE CHANGED -- see github issues
		//Request req = new Request(REQ_SPECIAL_CHARS, ORION_SERVER_WORKSPACE);
		//ArgGen argGen = new ArgGen(req);
		//assertEquals(argGen.toString(), "-g Java --path B!rdW@tch3r-S<YM^77-Java-Gen-Umple/" + ORION_SERVER_WORKSPACE + "/go/goon/OrionContent/My First Car/B!rdW@tch3r-S<YM^77.ump");
	}	

}

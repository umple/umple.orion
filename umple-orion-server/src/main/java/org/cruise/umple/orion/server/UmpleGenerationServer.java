package org.cruise.umple.orion.server;
import static spark.Spark.*;

public class UmpleGenerationServer {
	
	//TODO: set server path to server location
	private static final String ORION_SERVER_WORKSPACE = "/home/nwam/4475/eclipse/serverworkspace/";
	private static final String ORION_USER_CONTENT_DIRECTORY = "OrionContent/";
	
	private static final String REQUEST_FILE_PREFIX = "/file/";
	private static final String REQUEST_ORIONCONTENT = "-OrionContent";
	
    public static void main(String[] args) {
    	    	
    	/* ENABLE CORS */
    	options("/UmpleGenerate",
    	        (request, response) -> {

    	            String accessControlRequestHeaders = request
    	                    .headers("Access-Control-Request-Headers");
    	            if (accessControlRequestHeaders != null) {
    	                response.header("Access-Control-Allow-Headers",
    	                        accessControlRequestHeaders);
    	            }

    	            String accessControlRequestMethod = request
    	                    .headers("Access-Control-Request-Method");
    	            if (accessControlRequestMethod != null) {
    	                response.header("Access-Control-Allow-Methods",
    	                        accessControlRequestMethod);
    	            }

    	            return "OK";
    	        });
    	before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));
    	
 
    	/* HTTP REQUEST HANDLERS */
    	
        get("/UmpleGenerate", (req, res) -> {
        	System.out.println("Received GET request");
        	return "";
        });
        
        post("/UmpleGenerate", (req, res) -> {
        	System.out.println("Received request:\n" + req.body());
        	res.body("Info processed"); 
        	
        	// Parse request for username and filenames
        	String reqBody = req.body();
        	String username = reqBody.substring(REQUEST_FILE_PREFIX.length(), reqBody.indexOf(REQUEST_ORIONCONTENT));
        	String[] filenames = new String[]{reqBody.substring(reqBody.indexOf('/', REQUEST_FILE_PREFIX.length()))};
        	
        	// Get the user's directory
        	String userDirectory = String.format("%s/%s/%s/", 
        			ORION_SERVER_WORKSPACE, username.substring(0,2), username);
        	
        	// Run umple generator on each file
        	for (String relativeFilename : filenames){
        		String filename = String.format("%s/%s/%s", 
        				userDirectory, ORION_USER_CONTENT_DIRECTORY, relativeFilename);
        		cruise.umple.UmpleConsoleMain.main(new String[]{filename});
        	}
        	
        	return "";
        });
    }
}
package org.cruise.umple.orion.server;
import static spark.Spark.*;
import java.net.URLDecoder;

public class UmpleGenerationServer {
	
	//TODO: set server path to server location
	private static final String ORION_SERVER_WORKSPACE = "/home/nwam/4475/eclipse/serverworkspace/";
	private static final String ORION_USER_CONTENT_DIRECTORY = "OrionContent/";
	
	private static final String REQUEST_FILE_PREFIX = "/file/";
	private static final String REQUEST_ORIONCONTENT = "-OrionContent";
	
	private static final String UMPLE_GENERATE_FLAG = "-g";
	private static final String UMPLE_PATH_FLAG = "--path";
	private static final String UMPLE_GENERATED_FOLDER_POSTFIX = "-Gen-Umple";
	
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
        	System.out.println("[Received GET request]");
        	return "";
        });
        
        post("/UmpleGenerate", (req, res) -> {
        	System.out.println("[Received request]:\n" + req.body() + '\n' );
        	res.body("Info processed"); 
        	
        	// Parse request for username, filename, and generation language
        	// Current request format is <language>\n<filename> (filename contains username)
        	String[] reqBodyLines = URLDecoder.decode(req.body(), "UTF-8").split("\\r?\\n");
        	String language = reqBodyLines[0];
        	String username = reqBodyLines[1].substring(REQUEST_FILE_PREFIX.length(), reqBodyLines[1].indexOf(REQUEST_ORIONCONTENT));
        	String[] filenames = new String[]{reqBodyLines[1].substring(reqBodyLines[1].indexOf('/', REQUEST_FILE_PREFIX.length()))};
        	
        	// Get the user's directory path
        	String userDirectory = String.format("%s/%s/%s/", 
        			ORION_SERVER_WORKSPACE, username.substring(0,2), username);
        	
        	// Run umple generator on each file
        	for (String relativeFilename : filenames){
        		// Set up args
        		String filename = String.format("%s/%s/%s", 
        				userDirectory, ORION_USER_CONTENT_DIRECTORY, relativeFilename);
        		String[] umpleArgs;
        		if(!language.equals("")){
        			umpleArgs = new String[]{
        					UMPLE_GENERATE_FLAG, language, 
        					UMPLE_PATH_FLAG, filename.substring(filename.lastIndexOf('/')+1, filename.lastIndexOf('.'))
        							+ "-" + language + UMPLE_GENERATED_FOLDER_POSTFIX,
        					filename}; 
        		}else{
        			umpleArgs = new String[]{filename};
        		}
        		
        		// Execute Umple generation
        		cruise.umple.UmpleConsoleMain.main(umpleArgs);
        	}
        	
        	return "";
        });
    }
}
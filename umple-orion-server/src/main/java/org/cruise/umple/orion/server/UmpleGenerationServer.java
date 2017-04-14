package org.cruise.umple.orion.server;
import static spark.Spark.*;
import java.net.URLDecoder;

public class UmpleGenerationServer {
	
	/* If you are not developing/testing on the Docker image, you must change
	 * ORION_SERVER WORKSPACE to point to eclipse/serverworkspace of the Orion server 
	 * KEYSTORE_LOCATION to point keystore.jks on your machine
	 */
	private static final String ORION_SERVER_WORKSPACE = "/opt/eclipse/serverworkspace/"; // // "/home/nwam/4475/eclipse/serverworkspace/";
	private static final String ORION_USER_CONTENT_DIRECTORY = "OrionContent/";
	
	private static final String REQUEST_FILE_PREFIX = "/file/";
	private static final String REQUEST_ORIONCONTENT = "-OrionContent";
	
	private static final String UMPLE_GENERATE_FLAG = "-g";
	private static final String UMPLE_PATH_FLAG = "--path";
	private static final String UMPLE_GENERATED_FOLDER_POSTFIX = "-Gen-Umple";

    private static final String KEYSTORE_LOCATION =  "/opt/umple-orion-server/deploy/keystore.jks";// on the docker image "/home/nwam/4475/umple.orion/umple-orion-server/deploy/keystore.jks";
    private static final String KEYSTORE_PASSWORD = "password";
	
    public static void main(String[] args) {
    	    	
        // Enable HTTPS/SSL
        secure(KEYSTORE_LOCATION, KEYSTORE_PASSWORD, null, null);

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
    	
    	/* get has no functionality, just here for testing purposes */
        get("/UmpleGenerate", (req, res) -> {
        	System.out.println("[umple-orion-server] Received GET request");
        	return "";
        });
        
        /* generate code on the Orion server */
        post("/UmpleGenerate", (req, res) -> {
        	System.out.println("Received POST request:\n" + req.body() + '\n' );
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

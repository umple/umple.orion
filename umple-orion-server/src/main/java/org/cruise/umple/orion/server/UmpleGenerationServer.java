package org.cruise.umple.orion.server;
import static spark.Spark.*;

public class UmpleGenerationServer {
	
	/* If you are not developing/testing on the Docker image, you must change
	 * ORION_SERVER WORKSPACE to point to eclipse/serverworkspace of the Orion server 
	 * KEYSTORE_LOCATION to point keystore.jks on your machine
	 */
	//public static final String ORION_SERVER_WORKSPACE = "/opt/eclipse/serverworkspace/"; 
	public static final String ORION_SERVER_WORKSPACE = "/home/nwam/4475/eclipse/serverworkspace/";
	
	private static final String UMPLE_GENERATE_FLAG = "-g";
	private static final String UMPLE_PATH_FLAG = "--path";
	private static final String UMPLE_GENERATED_FOLDER_POSTFIX = "-Gen-Umple";

    //private static final String KEYSTORE_LOCATION =  "/opt/umple-orion-server/deploy/keystore.jks";
    private static final String KEYSTORE_LOCATION =  "/home/nwam/4475/umple.orion/umple-orion-server/deploy/keystore.jks";
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
        	
        	// Parse request for filename and generation language
        	Request request = new Request(req.body(), ORION_SERVER_WORKSPACE);
        	String language = request.getLanguage();
        	String filename = request.getAbsoluteFilename();
        	
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
        	
        	
        	return "";
        });
    }
}

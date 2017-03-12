import static spark.Spark.*;

import java.util.Arrays;

public class UmpleGenerationServer {
	
	//TODO: set server path to server location
	private static final String ORION_SERVER_WORKSPACE = "/home/nwam/4475/eclipse/serverworkspace/";
	private static final String ORION_USER_CONTENT_DIRECTORY = "OrionContent/";
	
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
        	System.out.println("GET!!!!");
        	res.body("test");
        	return "HI!";
        });
        
        post("/UmpleGenerate", (req, res) -> {
        	System.out.println("POST!!!!");
        	res.body("Info processed"); 
        	
        	// Parse request for username and filenames
        	String reqBody[] = req.body().split("\\r?\\n");
        	String username = reqBody[0];
        	String[] filenames = Arrays.copyOfRange(reqBody, 1, reqBody.length);
        	
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
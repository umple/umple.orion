import static spark.Spark.*;

public class Main {
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
    	
 
        get("/UmpleGenerate", (req, res) -> {
        	System.out.println("GET!!!!");
        	res.body("test");
        	return "HI!";
        });
        
        //get filename(s) from request
        //get username from request
        //run umple function(s) on file(s)
        
        post("/UmpleGenerate", (req, res) -> {
        	System.out.println("POST!!!!");
        	res.body("Info processed"); 
        	return req.body();
        });
    }
}
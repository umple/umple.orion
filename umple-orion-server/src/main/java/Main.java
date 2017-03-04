import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        get("/UmpleGenerate", (req, res) -> {
        	res.body("test");
        	return "HI!";
        });
        //get filename(s) from request
        //get username from request
        //run umple function(s) on file(s)
        get("/UmpleGenerate", (req, res) -> {
        	res.body("Info processed"); 
        	return req.body();
        });
    }
}
package org.cruise.umple.orion.server;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/* Request understands the requests we recieve
 * Request parses requests and returns useful information
 */
public class Request {

	private static final String REQUEST_FILE_PREFIX = "/file/";
	private static final String REQUEST_ORIONCONTENT = "-OrionContent";
	
	private String   language; // language to generate
	private String   username; // user making request
	private String   filename; // file path+name relative to root of user's file-system in Orion server
	
	private String orionServerWorkspace; // Absolute path to the root of where the Orion server stores user files (workspace)
	// Example request:
	// Java
	// /file/goon-OrionContent/My%20First%20Car/B!rdW@tch3r-S%3CYM%5E77.ump

	// Parse request to get request contents 
    // Request format is <language>\n<fileInfo>
    // <language> = language 
    // <fileInfo> = /file/<username>-OrionContent/<filename>
	public Request(String httpPostBody, String orionServerWorkspace){
		this.orionServerWorkspace = orionServerWorkspace;
		
    	String[] reqBodyLines = splitAndDecode(httpPostBody);
		
    	language = reqBodyLines[0];
    	String fileInfo = reqBodyLines[1];
    	
    	username = parseUsernameFromFileInfo(fileInfo);
    	filename = parseFilenameFromFileInfo(fileInfo);
	}
	
	// Return absolute path containing user's files in Orion server
	public String getUserDirectory(){
		return String.format("%s/%s/%s/", 
    			orionServerWorkspace, username.substring(0,2), username);
	}
	
	// split request into 2: <language> and <fileInfo>
	// and URL decode
	private static String[] splitAndDecode(String request){
		try {
			return URLDecoder.decode(request, "UTF-8").split("\\r?\\n");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// extracts <username> from the string:
	// /file/<username>-OrionContent/<filename>
	private static String parseUsernameFromFileInfo(String fileInfo){
		return fileInfo.substring(REQUEST_FILE_PREFIX.length(), fileInfo.indexOf(REQUEST_ORIONCONTENT));
	}
	
	// extracts <filename> from the string:
	// /file/<username>-OrionContent/<filename>
	private static String parseFilenameFromFileInfo(String fileInfo){
		return fileInfo.substring(fileInfo.indexOf('/', REQUEST_FILE_PREFIX.length()));
	}
	
	
	public String getLanguage(){
		return language;
	}
	
	public String getUsername(){
		return username;
	}
	
	public String getFilename(){
		return filename;
	}
}

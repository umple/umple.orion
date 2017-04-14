package org.cruise.umple.orion.server;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class Request {
	private static final String REQUEST_FILE_PREFIX = "/file/";
	private static final String REQUEST_ORIONCONTENT = "-OrionContent";
	
	private String   language; // language to generate
	private String   username; // user making request
	private String   filename; // file path+name relative to root of user's file-system in Orion server
	
	// Parse request to get request contents 
    // Request format is <language>\n<fileInfo>
    // <language> = language 
    // <fileInfo> = /file/<username>-OrionContent/<filename>
	public Request(String httpPostBody){
    	String[] reqBodyLines = null;
		try {
			reqBodyLines = URLDecoder.decode(httpPostBody, "UTF-8").split("\\r?\\n");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
    	language = reqBodyLines[0];
    	String fileInfo = reqBodyLines[1];
    	
    	username = parseUsernameFromFileInfo(fileInfo);
    	filename = parseFilenameFromFileInfo(fileInfo);
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

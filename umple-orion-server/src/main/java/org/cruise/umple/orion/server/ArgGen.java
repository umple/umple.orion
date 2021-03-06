package org.cruise.umple.orion.server;

public class ArgGen {
	private Request request;
	
	private static final String UMPLE_GENERATE_FLAG = "-g";
	private static final String UMPLE_PATH_FLAG = "--path";
	private static final String UMPLE_GENERATED_FOLDER_PREFIX = "src-";
	
	public ArgGen(Request r){
		request = r;
	}
	
	public String[] generateUmpleArgs(){
		String[] umpleArgs;
		String language = request.getLanguage();
		String filename = request.getAbsoluteFilename();
		
		if(!language.equals("")){
			umpleArgs = new String[]{
					UMPLE_GENERATE_FLAG, language, 
					UMPLE_PATH_FLAG, UMPLE_GENERATED_FOLDER_PREFIX + language + '/',
					filename}; 
		}else{
			umpleArgs = new String[]{filename};
		}
		
		return umpleArgs;
	}
	
	public String toString(){
		String s = "";
		for (String arg : generateUmpleArgs()){
			s += arg + " ";
		}
		return s.substring(0, s.length()-1);
	}

}

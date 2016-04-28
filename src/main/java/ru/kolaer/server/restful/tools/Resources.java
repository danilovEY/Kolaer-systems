package ru.kolaer.server.restful.tools;

public interface Resources {
	
	public static final String VERSION_0_0_1 = "0.0.1";
	
	public static final String VERSION = VERSION_0_0_1;
	
	public static final String ROOT_API = "/api";
	public static final String ROOT_API_VERSION = "/{version:.+}";
	public static final String APPLICATIONS = "/applications";
	
	
	public static final String ABSOLUTE_ROOT_API = "/api";
	public static final String ABSOLUTE_APPLICATIONS = ABSOLUTE_ROOT_API+"/"+VERSION+APPLICATIONS;

}

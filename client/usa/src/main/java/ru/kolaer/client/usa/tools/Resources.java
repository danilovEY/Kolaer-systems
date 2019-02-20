package ru.kolaer.client.usa.tools;

public interface Resources {
	String VERSION = "4.4";

	String PROPERTIES_NAME_URL_SERVER_MAIN = "url.server.main";
	String PROPERTIES_NAME_URL_SERVICE_CHAT = "url.service.chat";
	String PROPERTIES_NAME_MULTIPLE_INSTANT = "multiple.instant";
	String PROPERTIES_NAME_PATH_CACHE_RAND = "path.cache.rand";
	String PROPERTIES_NAME_PATH_CACHE = "path.cache";
	String PROPERTIES_NAME_PATH_PLUGINS = "path.plugins";
	String PROPERTIES_NAME_SERVICE_ENABLE = "service.enable";
	String PROPERTIES_NAME_TRAY_ENABLE = "tray.enable";

	String PROPERTIES_DEFAULT_VALUE_URL_SERVER_MAIN = "http://localhost:8080";
	String PROPERTIES_DEFAULT_VALUE_URL_SERVICE_CHAT = "ws://localhost:8080";
	String PROPERTIES_DEFAULT_VALUE_MULTIPLE_INSTANT = "false";
	String PROPERTIES_DEFAULT_VALUE_PATH_CACHE_RAND = "false";
	String PROPERTIES_DEFAULT_VALUE_PATH_CACHE = "cache";
	String PROPERTIES_DEFAULT_VALUE_PATH_PLUGINS = "plugins";
	String PROPERTIES_DEFAULT_VALUE_SERVICE_ENABLE = "true";
	String PROPERTIES_DEFAULT_VALUE_TRAY_ENABLE = "true";

	String CONFIG_PATH ="/config.properties";

}
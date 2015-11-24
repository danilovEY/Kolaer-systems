package ru.kolaer.asmc.tools;

import ru.kolaer.asmc.tools.serializations.SerializationGroups;

/**
 * Sigleton-настроек.
 * @author Danilov E.Y.
 *
 */
public class SettingSingleton {
	private transient static SettingSingleton inctance;
	
	/**/
	public static synchronized SettingSingleton getInstance() {
		if(inctance == null)
			inctance = new SettingSingleton();
		return inctance;
	}
	
	private volatile boolean isRoot = false;
	private final String ROOT_LOGIN_NAME = "root";
	private String rootPass = "root";
	private boolean isAllLabels = true;
	private boolean defaultWebBrowser = true;
	private String pathWebBrowser = "";
	private String pathBanner = "";
	
	private transient final SerializationGroups serializationGroups = new SerializationGroups();
	
	private SettingSingleton() {
		
	}

	public String getRootPass() {
		return rootPass;
	}

	public void setRootPass(String rootPass) {
		this.rootPass = rootPass;
	}

	public String getRootLoginName() {
		return ROOT_LOGIN_NAME;
	}

	public boolean isRoot() {
		return isRoot;
	}

	public void setRoot(boolean isRoot) {
		this.isRoot = isRoot;
	}

	/**
	 * @return the {@linkplain #serializationGroups}
	 */
	public SerializationGroups getSerializationGroups() {
		return serializationGroups;
	}
	
	public void saveGroups() {
		this.serializationGroups.setSerializeGroups(this.serializationGroups.getSerializeGroups());
	}

	public boolean isDefaultWebBrowser() {
		return defaultWebBrowser;
	}

	public void setDefaultWebBrowser(boolean defaultWebBrowser) {
		this.defaultWebBrowser = defaultWebBrowser;
	}

	public String getPathWebBrowser() {
		return pathWebBrowser;
	}

	public void setPathWebBrowser(String pathWebBrowser) {
		this.pathWebBrowser = pathWebBrowser;
	}

	public boolean isAllLabels() {
		return isAllLabels;
	}

	public void setAllLabels(boolean isAllLabels) {
		this.isAllLabels = isAllLabels;
	}

	public String getPathBanner() {
		return pathBanner;
	}

	public void setPathBanner(String pathBanner) {
		this.pathBanner = pathBanner;
	}
}

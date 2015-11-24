package ru.kolaer.asmc.tools;

import java.io.Serializable;

import ru.kolaer.asmc.tools.serializations.SerializationObjects;

/**
 * Sigleton-настроек.
 * @author Danilov E.Y.
 *
 */
public class SettingSingleton implements Serializable {

	private static final long serialVersionUID = -360823673740807137L;
	
	private transient static SettingSingleton inctance;
	
	/**/
	public static synchronized SettingSingleton getInstance() {
		return inctance;
	}
	
	public static synchronized void setInstance(SettingSingleton in) {
		inctance = in;
	}
	
	private boolean isRoot = false;
	private final String ROOT_LOGIN_NAME = "root";
	private String rootPass = "root";
	private boolean isAllLabels = true;
	private boolean defaultWebBrowser = true;
	private String pathWebBrowser = "";
	private String pathBanner = "";
	
	private transient SerializationObjects serializationObjects = new SerializationObjects();
	
	public SettingSingleton() {
		
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
	 * @return the {@linkplain #serializationObjects}
	 */
	public SerializationObjects getSerializationObjects() {
		return serializationObjects;
	}
	
	public void saveGroups() {
		this.serializationObjects.setSerializeGroups(this.serializationObjects.getSerializeGroups());
	}
	
	public void saveSetting() {
		this.serializationObjects.setSerializeSetting(this);
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

	public void setSerializationObjects(SerializationObjects serializationGroups) {
		this.serializationObjects = serializationGroups;
	}
}

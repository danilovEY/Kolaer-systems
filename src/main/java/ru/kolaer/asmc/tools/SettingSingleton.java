package ru.kolaer.asmc.tools;

/**
 * Sigleton-настроек.
 * @author Danilov E.Y.
 *
 */
public class SettingSingleton {
	private static final SettingSingleton inctance = new SettingSingleton();
	
	/**/
	public static synchronized SettingSingleton getInstance() {
		return inctance;
	}
	
	private volatile boolean isRoot = false;
	private final String ROOT_LOGIN_NAME = "root";
	private volatile String rootPass = "root";
	
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
}

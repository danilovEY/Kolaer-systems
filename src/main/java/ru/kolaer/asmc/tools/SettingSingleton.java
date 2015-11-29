package ru.kolaer.asmc.tools;

import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ru.kolaer.asmc.tools.serializations.SerializationObjects;

/**
 * Sigleton-настроек.
 * @author Danilov E.Y.
 *
 */
public class SettingSingleton implements Serializable {

	private static final long serialVersionUID = -360823673740807137L;

	private transient static SettingSingleton inctance;
	private static final transient ExecutorService executor = Executors.newFixedThreadPool(2);
	private static transient Future<Boolean> futureInitSetting ;
	
	private transient boolean isRoot = false;
	private final String ROOT_LOGIN_NAME = "root";
	private String rootPass = "root";
	/**Правило запуска для всех ярлыков.*/
	private boolean isAllLabels = true;
	/**Запуск через внутреннего браузера.*/
	private boolean defaultWebBrowser = true;
	/**Запуск через браузер пользователя.*/
	private boolean defaultUserWebBrowser = false;
	/**Путь к браузеру.*/
	private String pathWebBrowser = "";
	/**Путь к банеру.*/
	private String pathBanner = "";
	/**Сериализованные объекты.*/
	private transient SerializationObjects serializationObjects;
	
	private SettingSingleton() {
	}

	public static synchronized SettingSingleton getInstance() {
		return inctance;
	}
	
	public static synchronized void initialization() {
		final SerializationObjects serializationObjects = new SerializationObjects();
		futureInitSetting = executor.submit(() -> {	
			SettingSingleton.inctance = serializationObjects.getSerializeSetting();
			if(SettingSingleton.inctance == null) {
				SettingSingleton.inctance = new SettingSingleton();
				serializationObjects.setSerializeSetting(SettingSingleton.inctance);			
			}
			SettingSingleton.inctance.setSerializationObjects(serializationObjects);
			
			return true;
		});
		
		executor.submit(() -> {
			serializationObjects.getSerializeGroups();
			return;
		});
		
		executor.shutdown();
	}
	
	public static synchronized boolean isInitialized() {
		return futureInitSetting.isDone();
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

	public boolean isDefaultUserWebBrowser() {
		return defaultUserWebBrowser;
	}

	public void setDefaultUserWebBrowser(boolean defaultUserWebBrowser) {
		this.defaultUserWebBrowser = defaultUserWebBrowser;
	}
}

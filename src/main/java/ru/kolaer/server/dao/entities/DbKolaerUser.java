package ru.kolaer.server.dao.entities;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ru.kolaer.server.restful.tools.UserLog;

@JsonIgnoreProperties("log")
public class DbKolaerUser implements Serializable {
	private static final long serialVersionUID = -4533583948766410710L;
	
	private final String name;
	private final transient UserLog log;
	private boolean ping = true;
	
	private String ip = "";
	private String hostName = "";
	private List<String> openingWindows = new LinkedList<>();
	private List<String> closingWindows = new LinkedList<>();
	
	public DbKolaerUser(final String user) {
		this.log = new UserLog(user);
		this.name = user;
		this.log.addSystemMessage("Пользователь \"" + this.name + "\" добавлен!");
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getIp() {
		return this.ip;
	}

	public String getHostName() {
		return this.hostName;
	}
	
	public void addHostName(final String hostName) {
		this.hostName = hostName;
		this.log.addSystemMessage("Добавлено имя компьютера: " + hostName);
	}
	
	public List<String> getOpeningWindows() {
		return this.openingWindows;
	}

	public void addIp(final String ip) {
		this.ip = ip;
		this.log.addSystemMessage("Добавлен IP: " + ip);
	}
	
	public void addOpeningApplication(final String name) {
		if(this.openingWindows.add(name)){
			this.log.addSystemMessage("Окно \"" + name  + "\" открыто!");
		}
	}
	
	public void addCloseApplication(final String name) {
		this.closingWindows.add(name);
	}
	
	public List<String> getCloseApplications() {
		return this.closingWindows;
	}
	
	public void removeOpeningApplication(final String name) {
		if(this.openingWindows.remove(name)){
			this.log.addSystemMessage("Окно \"" + name  + "\" закрыто!");
		}
	}
	
	public void addKey(final String key) {
		this.log.addTelemetryMessage(key);
	}
	
	public UserLog getLog() {
		return this.log;
	}
	
	public void disconect() {
		this.log.addSystemMessage("Пользователь \"" + this.name + "\" удален!");
		this.log.shutdown();
		this.ip = "";
		this.closingWindows.clear();
		this.openingWindows.clear();
	}
	/**
	 * @return the {@linkplain #ping}
	 */
	public boolean isPing() {
		return this.ping;
	}
	/**
	 * @param ping the {@linkplain #ping} to set
	 */
	public void setPing(boolean ping) {
		this.ping = ping;
	}
}
